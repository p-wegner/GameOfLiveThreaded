package game.gameoflife;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import game.api.NetworkCell;
import game.globals.Stats;
import game.network.NavigatableCell;
import game.network.messages.Message;
import game.network.messages.NeighborRoutingInfo;

public class LifeCommunicator {

	private List<AliveRequest> requests = Collections.synchronizedList(new ArrayList<>());
	private List<AliveResponse> responses = Collections.synchronizedList(new ArrayList<>());
	private MessageId messageId;
	private GameOfLifeCell gameOfLifeCell;
	private boolean debug = false;

	public LifeCommunicator(GameOfLifeCell gameOfLifeCell, int generationInterval) {
		this.gameOfLifeCell = gameOfLifeCell;
		this.messageId = new MessageId(generationInterval);
	}

	public void requestNeighborAliveStatus() {
		gameOfLifeCell.sendNeighbors(routing -> routing.send(new AliveRequest(routing, messageId.getId())));
	}

	public List<AliveResponse> evaluateAliveResponses() {
		List<AliveResponse> responsesCopy = atomicSwitchResponses();

		long filtered = responsesCopy.stream().filter(response -> response.getMessageId() == messageId.getId()).count();
		long all = responsesCopy.size();
		Stats.histo("filteredresponses", all - filtered);

		List<AliveResponse> alive = responsesCopy.stream()
				.filter(response -> response.getMessageId() == messageId.getId())
				.collect(Collectors.toList());
		return alive;
	}

	public void answerAliveRequests() {
		if (!gameOfLifeCell.isAlive()) {
			return;
		}

		List<AliveRequest> requestsCopy = atomicSwitchRequests();
		Stats.histo("requests", requestsCopy.size());
		requestsCopy.forEach(request -> {
			NeighborRoutingInfo routing = request.getRouting();

			if (this.debug) {
				validateRequest(routing.getSourceNode(), routing.getTargetNode());
			}

			request.getRouting().respond(new AliveResponse(request));
		});
	}

	private List<AliveResponse> atomicSwitchResponses() {
		List<AliveResponse> responsesCopy = responses;
		responses = Collections.synchronizedList(new ArrayList<>());
		return responsesCopy;
	}

	private List<AliveRequest> atomicSwitchRequests() {
		List<AliveRequest> requestsCopy = requests;
		requests = Collections.synchronizedList(new ArrayList<>());
		return requestsCopy;
	}

	public void handle(Message message) {
		if (gameOfLifeCell.isAlive()) {
			message.ifPresent(AliveRequest.class, request -> requests.add(request));
		}

		message.ifPresent(AliveResponse.class, response -> responses.add(response));
	}

	public boolean timeForNextAliveRequest(int tick) {
		return messageId.isElapsed(tick);
	}

	// TODO signature not awesome
	public Optional<List<AliveResponse>> communicate(int tick) {
		answerAliveRequests();

		if (timeForNextAliveRequest(tick)) {
			List<AliveResponse> alive = evaluateAliveResponses();

			messageId.nextId(tick);
			// TODO getter not cool
			requestNeighborAliveStatus();
			return Optional.of(alive);
		}
		return Optional.empty();
	}

	// debugging
	public void validateRequest(NetworkCell sourceNode, NetworkCell targetNode) {
		NavigatableCell container = gameOfLifeCell.getContainer();
		if (targetNode != container) {
			Stats.inc("!!WRONGTARGET");
			throw new RuntimeException("TARGETNODE WRONG");
		}
		if (!container.containsNeighbor(sourceNode)) {
			Stats.inc("!!WRONGSOURCE");
			throw new RuntimeException("This is not my neighbor!!");
		}
	}

}
