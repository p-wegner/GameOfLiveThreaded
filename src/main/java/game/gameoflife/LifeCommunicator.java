package game.gameoflife;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import game.network.NavigatableCell;
import game.network.messages.Message;
import game.network.messages.NeighborRoutingInfo;

public class LifeCommunicator {

	private List<AliveRequest> requests = Collections.synchronizedList(new ArrayList<>());
	private List<AliveResponse> responses = Collections.synchronizedList(new ArrayList<>());
	private MessageId messageId = new MessageId(0);
	private GameOfLifeCell gameOfLifeCell;

	public LifeCommunicator(GameOfLifeCell gameOfLifeCell) {
		this.gameOfLifeCell = gameOfLifeCell;
	}

	public void requestNeighborAliveStatus(NavigatableCell container) {
		// TODO cell.sendNeighbors(msg)
		container.neighborsRoutingStream()
				.forEach(routing -> {
					AliveRequest request = new AliveRequest(routing, messageId.getId());
					routing.send(request);
				});
	}

	public List<AliveResponse> evaluateAliveResponses() {
		List<AliveResponse> responsesCopy = atomicSwitchResponses();
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
		requestsCopy.forEach(request -> {
			NeighborRoutingInfo routing = request.getRouting();
			gameOfLifeCell.validateRequest(routing.getSourceNode(), routing.getTargetNode());
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
		message.ifPresent(AliveRequest.class, request -> requests.add(request));
		message.ifPresent(AliveResponse.class, request -> responses.add(request));
	}

	public boolean timeForNextAliveRequest(int tick) {
		return messageId.isElapsed(tick);
	}

	// TODO not too awesome signature
	public Optional<List<AliveResponse>> communicate(int tick) {
		answerAliveRequests();

		if (timeForNextAliveRequest(tick)) {
			List<AliveResponse> alive = evaluateAliveResponses();

			messageId.nextId(tick);
			// TODO getter not cool
			requestNeighborAliveStatus(gameOfLifeCell.getContainer());
			return Optional.of(alive);
		}
		return Optional.empty();
	}
}
