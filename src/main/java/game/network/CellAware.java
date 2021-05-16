package game.network;

import game.api.CellCommunicator;
import game.network.factory.HasContainerReference;

public interface CellAware extends HasContainerReference<NavigatableCell>, CellCommunicator {

}
