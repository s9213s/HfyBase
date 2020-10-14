package com.deyi.hfybase.base;

import android.view.View;

import com.deyi.hfybase.baseModule.base.MBaseFragment;
import com.deyi.hfybase.util.SpUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hfy
 * @description
 * @time 2020/10/13
 */
public abstract class BaseFragment extends MBaseFragment {
    private Unbinder unbinder;
    public SpUtil spUtil;

    @Override
    public void init() {
        spUtil = new SpUtil(mContext);
    }

    @Override
    public void bindButterKnife(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void unBindButterKnife() {
        unbinder.unbind();
    }

}
