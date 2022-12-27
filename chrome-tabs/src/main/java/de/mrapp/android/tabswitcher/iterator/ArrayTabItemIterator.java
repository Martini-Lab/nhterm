/*
 * Copyright 2016 - 2017 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.tabswitcher.iterator;

import androidx.annotation.NonNull;
import de.mrapp.android.tabswitcher.Tab;
import de.mrapp.android.tabswitcher.model.TabItem;
import de.mrapp.android.util.view.AttachedViewRecycler;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An iterator, which allows to iterate the tab items, which correspond to the tabs, which are
 * contained by an array.
 *
 * @author Michael Rapp
 * @since 0.1.0
 */
public class ArrayTabItemIterator extends AbstractTabItemIterator {

  /**
   * A builder, which allows to configure an create instances of the class {@link
   * ArrayTabItemIterator}.
   */
  public static class Builder extends AbstractBuilder<Builder, ArrayTabItemIterator> {

    /**
     * The view recycler, which allows to inflate the views, which are used to visualize the
     * tabs, which are iterated by the iterator, which is created by the builder.
     */
    private final AttachedViewRecycler<TabItem, ?> viewRecycler;

    /**
     * The array, which contains the tabs, which are iterated by the iterator, which is created
     * by the builder.
     */
    private final Tab[] array;

    /**
     * Creates a new builder, which allows to configure and create instances of the class {@link
     * ArrayTabItemIterator}.
     *
     * @param viewRecycler The view recycler, which allows to inflate the views, which are used to visualize
     *                     the tabs, which should be iterated by the iterator, as an instance of the class
     *                     AttachedViewRecycler. The view recycler may not be null
     * @param array        The array, which contains the tabs, which should be iterated by the iterator, as
     *                     an array of the type {@link Tab}. The array may not be null
     */
    public Builder(@NonNull final AttachedViewRecycler<TabItem, ?> viewRecycler,
                   @NonNull final Tab[] array) {
      ensureNotNull(viewRecycler, "The view recycler may not be null");
      ensureNotNull(array, "The array may not be null");
      this.viewRecycler = viewRecycler;
      this.array = array;
    }

    @NonNull
    @Override
    public ArrayTabItemIterator create() {
      return new ArrayTabItemIterator(viewRecycler, array, reverse, start);
    }

  }

  /**
   * The view recycler, which allows to inflate the views, which are used to visualize the
   * iterated tabs.
   */
  private final AttachedViewRecycler<TabItem, ?> viewRecycler;

  /**
   * The array, which contains the tabs, which are iterated by the iterator.
   */
  private final Tab[] array;

  /**
   * Creates a new iterator, which allows to iterate the tab items, whcih correspond to the tabs,
   * which are contained by an array.
   *
   * @param viewRecycler The view recycler, which allows to inflate the views, which are used to visualize the
   *                     iterated tabs, as an instance of the class AttachedViewRecycler. The view recycler
   *                     may not be null
   * @param array        The array, which contains the tabs, which should be iterated by the iterator, as an
   *                     array of the type {@link Tab}. The array may not be null
   * @param reverse      True, if the tabs should be iterated in reverse order, false otherwise
   * @param start        The index of the first tab, which should be iterated, as an {@link Integer} value or
   *                     -1, if all tabs should be iterated
   */
  private ArrayTabItemIterator(@NonNull final AttachedViewRecycler<TabItem, ?> viewRecycler,
                               @NonNull final Tab[] array, final boolean reverse,
                               final int start) {
    ensureNotNull(viewRecycler, "The view recycler may not be null");
    ensureNotNull(array, "The array may not be null");
    this.viewRecycler = viewRecycler;
    this.array = array;
    initialize(reverse, start);
  }

  @Override
  public final int getCount() {
    return array.length;
  }

  @NonNull
  @Override
  public final TabItem getItem(final int index) {
    return TabItem.create(viewRecycler, index, array[index]);
  }

}
