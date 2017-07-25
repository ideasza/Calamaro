package update;

public class UpdatePresenter implements UpdateInterface.Presenter{
	
	private UpdateInterface.View view;
	public UpdatePresenter(UpdateInterface.View view) {
		this.view = view;
	}

}
