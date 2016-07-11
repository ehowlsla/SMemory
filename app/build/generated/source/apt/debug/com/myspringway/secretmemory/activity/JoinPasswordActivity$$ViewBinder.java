// Generated code from Butter Knife. Do not modify!
package com.myspringway.secretmemory.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import butterknife.internal.ViewBinder;
import java.lang.CharSequence;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class JoinPasswordActivity$$ViewBinder<T extends JoinPasswordActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624064, "field 'password' and method 'passwordTextChange'");
    target.password = finder.castView(view, 2131624064, "field 'password'");
    unbinder.view2131624064 = view;
    ((TextView) view).addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.passwordTextChange(p0, p1, p2, p3);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    });
    view = finder.findRequiredView(source, 2131624065, "field 'password_re' and method 'passwordReTextChange'");
    target.password_re = finder.castView(view, 2131624065, "field 'password_re'");
    unbinder.view2131624065 = view;
    ((TextView) view).addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.passwordReTextChange(p0, p1, p2, p3);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    });
    view = finder.findRequiredView(source, 2131624062, "field 'prev' and method 'prevPage'");
    target.prev = finder.castView(view, 2131624062, "field 'prev'");
    unbinder.view2131624062 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.prevPage();
      }
    });
    view = finder.findRequiredView(source, 2131624063, "field 'next' and method 'nextPage'");
    target.next = finder.castView(view, 2131624063, "field 'next'");
    unbinder.view2131624063 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.nextPage();
      }
    });
    view = finder.findRequiredView(source, 2131624058, "field 'root_layout'");
    target.root_layout = finder.castView(view, 2131624058, "field 'root_layout'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends JoinPasswordActivity> implements Unbinder {
    private T target;

    View view2131624064;

    View view2131624065;

    View view2131624062;

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
      ((TextView) view2131624064).addTextChangedListener(null);
      target.password = null;
      ((TextView) view2131624065).addTextChangedListener(null);
      target.password_re = null;
      view2131624062.setOnClickListener(null);
      target.prev = null;
      view2131624063.setOnClickListener(null);
      target.next = null;
      target.root_layout = null;
    }
  }
}
