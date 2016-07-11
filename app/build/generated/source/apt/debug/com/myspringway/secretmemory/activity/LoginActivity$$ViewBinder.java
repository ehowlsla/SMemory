// Generated code from Butter Knife. Do not modify!
package com.myspringway.secretmemory.activity;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class LoginActivity$$ViewBinder<T extends LoginActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624061, "field 'email'");
    target.email = finder.castView(view, 2131624061, "field 'email'");
    view = finder.findRequiredView(source, 2131624064, "field 'password'");
    target.password = finder.castView(view, 2131624064, "field 'password'");
    view = finder.findRequiredView(source, 2131624068, "method 'goFacebookLogin'");
    unbinder.view2131624068 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goFacebookLogin();
      }
    });
    view = finder.findRequiredView(source, 2131624066, "method 'goLogin'");
    unbinder.view2131624066 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goLogin();
      }
    });
    view = finder.findRequiredView(source, 2131624069, "method 'goJoin'");
    unbinder.view2131624069 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goJoin();
      }
    });
    view = finder.findRequiredView(source, 2131624070, "method 'goFind'");
    unbinder.view2131624070 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goFind();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends LoginActivity> implements Unbinder {
    private T target;

    View view2131624068;

    View view2131624066;

    View view2131624069;

    View view2131624070;

    protected InnerUnbinder(T target) {
      this.target = target;
    }

    @Override
    public final void unbind() {
      if (target == null) throw new IllegalStateException("Bindings already cleared.");
      unbind(target);
      target = null;
    }

    protected void unbind(T target) {
      target.email = null;
      target.password = null;
      view2131624068.setOnClickListener(null);
      view2131624066.setOnClickListener(null);
      view2131624069.setOnClickListener(null);
      view2131624070.setOnClickListener(null);
    }
  }
}
