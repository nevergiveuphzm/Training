// Generated code from Butter Knife. Do not modify!
package com.oucb303.training.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BaseTrainingActivity$$ViewBinder<T extends com.oucb303.training.activity.BaseTrainingActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296336, "field 'btnTimesRandom' and method 'onClick'");
    target.btnTimesRandom = finder.castView(view, 2131296336, "field 'btnTimesRandom'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296337, "field 'btnTimeRandom' and method 'onClick'");
    target.btnTimeRandom = finder.castView(view, 2131296337, "field 'btnTimeRandom'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296338, "field 'btnSequenceTraining' and method 'onClick'");
    target.btnSequenceTraining = finder.castView(view, 2131296338, "field 'btnSequenceTraining'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296339, "field 'btnSequences' and method 'onClick'");
    target.btnSequences = finder.castView(view, 2131296339, "field 'btnSequences'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296364, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnTimesRandom = null;
    target.btnTimeRandom = null;
    target.btnSequenceTraining = null;
    target.btnSequences = null;
  }
}
