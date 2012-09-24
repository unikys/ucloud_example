package com.kt.ucloud;

import com.kt.ucloud.api.UcloudApiManager;


public class UcloudExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		UcloudApiManager manager = new UcloudApiManager("8dfad1945247e11e7f7d893647a3516b","c65453cf50d82f0865ed747b1f3b76fe");
		new UcloudGUI(manager);
	}

}
