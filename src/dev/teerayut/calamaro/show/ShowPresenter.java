package dev.teerayut.calamaro.show;

public class ShowPresenter implements ShowInterface.Presenter {
	
	private ShowInterface.View view;
	public ShowPresenter(ShowInterface.View view) {
		this.view = view;
	}

}
