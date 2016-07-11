// Generated code from Butter Knife. Do not modify!
package com.myspringway.secretmemory.fragment;

import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class CardFragment$$ViewBinder<T extends CardFragment> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624101, "field 'swipe_deck'");
    target.swipe_deck = finder.castView(view, 2131624101, "field 'swipe_deck'");
    view = finder.findRequiredView(source, 2131624102, "method 'goWrite'");
    unbinder.view2131624102 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goWrite();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CardFragment> implements Unbinder {
    private T target;

    View view2131624102;

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
      target.swipe_deck = null;
      view2131624102.setOnClickListener(null);
    }
  }
}
