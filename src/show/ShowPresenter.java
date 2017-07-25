package show;

public class ShowPresenter implements ShowInterface.Presenter {
	
	private ShowInterface.View view;
	public ShowPresenter(ShowInterface.View view) {
		this.view = view;
	}

}
