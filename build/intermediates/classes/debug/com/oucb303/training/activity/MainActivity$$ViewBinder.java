// Generated code from Butter Knife. Do not modify!
package com.oucb303.training.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.oucb303.training.activity.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296344, "field 'btnLevelOne' and method 'onClick'");
    target.btnLevelOne = finder.castView(view, 2131296344, "field 'btnLevelOne'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296345, "field 'btnLevelTwo' and method 'onClick'");
    target.btnLevelTwo = finder.castView(view, 2131296345, "field 'btnLevelTwo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296346, "field 'btnLevelThree' and method 'onClick'");
    target.btnLevelThree = finder.castView(view, 2131296346, "field 'btnLevelThree'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296347, "field 'btnLevelFour' and method 'onClick'");
    target.btnLevelFour = finder.castView(view, 2131296347, "field 'btnLevelFour'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296348, "field 'btnBaseTraining' and method 'onClick'");
    target.btnBaseTraining = finder.castView(view, 2131296348, "field 'btnBaseTraining'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296349, "field 'btnStatistic' and method 'onClick'");
    target.btnStatistic = finder.castView(view, 2131296349, "field 'btnStatistic'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296350, "field 'btn23'");
    target.btn23 = finder.castView(view, 2131296350, "field 'btn23'");
    view = finder.findRequiredView(source, 2131296351, "field 'btn24'");
    target.btn24 = finder.castView(view, 2131296351, "field 'btn24'");
    view = finder.findRequiredView(source, 2131296341, "field 'btnCheck' and method 'onClick'");
    target.btnCheck = finder.castView(view, 2131296341, "field 'btnCheck'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296343, "field 'lvBattery'");
    target.lvBattery = finder.castView(view, 2131296343, "field 'lvBattery'");
  }

  @Override public void unbind(T target) {
    target.btnLevelOne = null;
    target.btnLevelTwo = null;
    target.btnLevelThree = null;
    target.btnLevelFour = null;
    target.btnBaseTraining = null;
    target.btnStatistic = null;
    target.btn23 = null;
    target.btn24 = null;
    target.btnCheck = null;
    target.lvBattery = null;
  }
}
