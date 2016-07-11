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

public class SignActivity$$ViewBinder<T extends SignActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624074, "field 'mPager'");
    target.mPager = finder.castView(view, 2131624074, "field 'mPager'");
    view = finder.findRequiredView(source, 2131624063, "field 'next' and method 'nextPage'");
    target.next = finder.castView(view, 2131624063, "field 'next'");
    unbinder.view2131624063 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.nextPage();
      }
    });
    view = finder.findRequiredView(source, 2131624075, "field 'indicator_default'");
    target.indicator_default = finder.castView(view, 2131624075, "field 'indicator_default'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends SignActivity> implements Unbinder {
    private T target;

    View view2131624063;

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
      target.mPager = null;
      view2131624063.setOnClickListener(null);
      target.next = null;
      target.indicator_default = null;
    }
  }
}
