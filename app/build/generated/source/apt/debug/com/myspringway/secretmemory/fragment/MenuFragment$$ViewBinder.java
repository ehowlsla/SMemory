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

public class MenuFragment$$ViewBinder<T extends MenuFragment> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624104, "field 'row1_layout' and method 'onClickRow1'");
    target.row1_layout = finder.castView(view, 2131624104, "field 'row1_layout'");
    unbinder.view2131624104 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRow1();
      }
    });
    view = finder.findRequiredView(source, 2131624105, "field 'row2_layout' and method 'onClickRow2'");
    target.row2_layout = finder.castView(view, 2131624105, "field 'row2_layout'");
    unbinder.view2131624105 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRow2();
      }
    });
    view = finder.findRequiredView(source, 2131624106, "field 'row3_layout' and method 'onClickRow3'");
    target.row3_layout = finder.castView(view, 2131624106, "field 'row3_layout'");
    unbinder.view2131624106 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRow3();
      }
    });
    view = finder.findRequiredView(source, 2131624107, "field 'row4_layout' and method 'onClickRow4'");
    target.row4_layout = finder.castView(view, 2131624107, "field 'row4_layout'");
    unbinder.view2131624107 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRow4();
      }
    });
    view = finder.findRequiredView(source, 2131624103, "field 'signOut' and method 'onSignOutClick'");
    target.signOut = finder.castView(view, 2131624103, "field 'signOut'");
    unbinder.view2131624103 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignOutClick();
      }
    });
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends MenuFragment> implements Unbinder {
    private T target;

    View view2131624104;

    View view2131624105;

    View view2131624106;

    View view2131624107;

    View view2131624103;

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
      view2131624104.setOnClickListener(null);
      target.row1_layout = null;
      view2131624105.setOnClickListener(null);
      target.row2_layout = null;
      view2131624106.setOnClickListener(null);
      target.row3_layout = null;
      view2131624107.setOnClickListener(null);
      target.row4_layout = null;
      view2131624103.setOnClickListener(null);
      target.signOut = null;
    }
  }
}
