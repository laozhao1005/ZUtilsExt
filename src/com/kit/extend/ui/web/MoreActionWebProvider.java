package com.kit.extend.ui.web;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.kit.extend.R;
import com.kit.utils.BrowserUtils;
import com.kit.utils.ClipboardUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ToastUtils;
import com.kit.utils.log.ZogUtils;

public class MoreActionWebProvider extends ActionProvider {

    //    private Context context;
    WebActivity activity;

    public MoreActionWebProvider(Context context) {
        super(context);
    }

    public void setActivity(WebActivity activity) {
        this.activity = activity;

        ZogUtils.i( "activity:" + activity);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {

        subMenu.clear();

        subMenu.add(activity.getString(R.string.menu_web_open_in_bowser))
                .setIcon(R.drawable.ic_bowser_white)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ZogUtils.e("activity.webFragment.loadingUrl:" + activity.webFragment.loadingUrl);
                        if (!StringUtils.isEmptyOrNullOrNullStr(activity.webFragment.loadingUrl))
                            BrowserUtils.gotoBrowser(activity, activity.webFragment.loadingUrl);
                        return true;
                    }
                });

        subMenu.add(activity.getString(R.string.menu_web_copy_url))
                .setIcon(R.drawable.ic_menu_copy)
                .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        ZogUtils.e("activity.webFragment.loadingUrl:" + activity.webFragment.loadingUrl);
                        if (!StringUtils.isEmptyOrNullOrNullStr(activity.webFragment.loadingUrl)) {
                            ClipboardUtils.copy(activity.webFragment.loadingUrl);
                            ToastUtils.mkShortTimeToast(activity.getResources().getString(R.string.copy_ok_default));
                        }
                        return true;
                    }
                });
    }


    @Override
    public boolean hasSubMenu() {
        return true;
    }

}