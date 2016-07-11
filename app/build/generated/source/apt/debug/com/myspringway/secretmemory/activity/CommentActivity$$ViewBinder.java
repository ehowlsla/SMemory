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

public class CommentActivity$$ViewBinder<T extends CommentActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624055, "field 'mCommentText'");
    target.mCommentText = finder.castView(view, 2131624055, "field 'mCommentText'");
    view = finder.findRequiredView(source, 2131624056, "field 'mCommentBtn' and method 'onCommnetBtnClicked'");
    target.mCommentBtn = finder.castView(view, 2131624056, "field 'mCommentBtn'");
    unbinder.view2131624056 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCommnetBtnClicked(p0);
      }
    });
    view = finder.findRequiredView(source, 2131624057, "field 'mCommentRecycler'");
    target.mCommentRecycler = finder.castView(view, 2131624057, "field 'mCommentRecycler'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends CommentActivity> implements Unbinder {
    private T target;

    View view2131624056;

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
      target.mCommentText = null;
      view2131624056.setOnClickListener(null);
      target.mCommentBtn = null;
      target.mCommentRecycler = null;
    }
  }
}
