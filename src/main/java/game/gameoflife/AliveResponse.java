package game.gameoflife;

public class AliveResponse {

	private AliveRequest request;

	public AliveResponse(AliveRequest request) {
		this.request = request;
	}

	public int getMessageId() {
		return request.getMessageId();
	}
}
