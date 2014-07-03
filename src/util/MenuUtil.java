package util;

import bitmaps.MenuBitmaps;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.util.StringProvider;
import view.MainView;
import view.ReviewView;
import view.group.GroupView;
import view.setting.SettingView;

public class MenuUtil {
	public static void attachMenu(MainScreen scr){
		MenuItem home=new MenuItem(new StringProvider("Home"), 110, 10){
			public void run() {
				Navigation.navigate(new MainView());
			}
		};
		home.setIcon(MenuBitmaps.getHomeIcon());
		MenuItem review=new MenuItem(new StringProvider("Review"), 110, 10){
			public void run() {
				Navigation.navigate(new ReviewView());
			}
		};
		review.setIcon(MenuBitmaps.getReviewIcon());
		MenuItem groups=new MenuItem(new StringProvider("Groups"), 110, 10){
			public void run() {
				Navigation.navigate(new GroupView());
			}
		};
		groups.setIcon(MenuBitmaps.getContactIcon());
		MenuItem setting=new MenuItem(new StringProvider("More"), 110, 10){
			public void run() {
				Navigation.navigate(new SettingView());
			}
		};
		setting.setIcon(MenuBitmaps.getSettingIcon());
		scr.removeAllMenuItems();
		scr.addMenuItem(home);
		scr.addMenuItem(review);
		scr.addMenuItem(groups);
		scr.addMenuItem(setting);
	}
}
