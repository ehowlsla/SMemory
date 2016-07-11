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

public class WriteActivity$$ViewBinder<T extends WriteActivity> implements ViewBinder<T> {
  @Override
  public Unbinder bind(final Finder finder, final T target, Object source) {
    InnerUnbinder unbinder = createUnbinder(target);
    View view;
    view = finder.findRequiredView(source, 2131624076, "field 'bg'");
    target.bg = finder.castView(view, 2131624076, "field 'bg'");
    view = finder.findRequiredView(source, 2131624079, "field 'close' and method 'onClose'");
    target.close = finder.castView(view, 2131624079, "field 'close'");
    unbinder.view2131624079 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClose();
      }
    });
    view = finder.findRequiredView(source, 2131624080, "field 'save' and method 'goSave'");
    target.save = finder.castView(view, 2131624080, "field 'save'");
    unbinder.view2131624080 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goSave();
      }
    });
    view = finder.findRequiredView(source, 2131624081, "field 'body'");
    target.body = finder.castView(view, 2131624081, "field 'body'");
    view = finder.findRequiredView(source, 2131624083, "field 'tag_body' and method 'goTagChange'");
    target.tag_body = finder.castView(view, 2131624083, "field 'tag_body'");
    unbinder.view2131624083 = view;
    ((TextView) view).addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
        target.goTagChange(p0, p1, p2, p3);
      }

      @Override
      public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
      }

      @Override
      public void afterTextChanged(Editable p0) {
      }
    });
    view = finder.findRequiredView(source, 2131624085, "field 'album' and method 'goAlbum'");
    target.album = finder.castView(view, 2131624085, "field 'album'");
    unbinder.view2131624085 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goAlbum();
      }
    });
    view = finder.findRequiredView(source, 2131624086, "field 'hash' and method 'goHash'");
    target.hash = finder.castView(view, 2131624086, "field 'hash'");
    unbinder.view2131624086 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.goHash();
      }
    });
    view = finder.findRequiredView(source, 2131624078, "field 'toolbar_layout'");
    target.toolbar_layout = finder.castView(view, 2131624078, "field 'toolbar_layout'");
    view = finder.findRequiredView(source, 2131624058, "field 'root_layout'");
    target.root_layout = finder.castView(view, 2131624058, "field 'root_layout'");
    view = finder.findRequiredView(source, 2131624084, "field 'tag_values'");
    target.tag_values = finder.castView(view, 2131624084, "field 'tag_values'");
    return unbinder;
  }

  protected InnerUnbinder<T> createUnbinder(T target) {
    return new InnerUnbinder(target);
  }

  protected static class InnerUnbinder<T extends WriteActivity> implements Unbinder {
    private T target;

    View view2131624079;

    View view2131624080;

    View view2131624083;

    View view2131624085;

    View view2131624086;

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
      target.bg = null;
      view2131624079.setOnClickListener(null);
      target.close = null;
      view2131624080.setOnClickListener(null);
      target.save = null;
      target.body = null;
      ((TextView) view2131624083).addTextChangedListener(null);
      target.tag_body = null;
      view2131624085.setOnClickListener(null);
      target.album = null;
      view2131624086.setOnClickListener(null);
      target.hash = null;
      target.toolbar_layout = null;
      target.root_layout = null;
      target.tag_values = null;
    }
  }
}
