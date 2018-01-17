package net.teamfruit.simpleloadingscreen.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ForwardingIterator;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ForwardingListIterator;

@Deprecated
public class InheritList<E> extends ForwardingList<E> {

	private final List<E> main;
	private final List<E> inherit;

	public InheritList(final List<E> main, final List<E> inherit) {
		this.main = main;
		this.inherit = inherit;
	}

	@Override
	protected List<E> delegate() {
		return this.main;
	}

	@Override
	public int size() {
		return this.inherit.size()+super.size();
	}

	@Override
	public boolean contains(final Object object) {
		return this.inherit.contains(object)||super.contains(object);
	}

	@Override
	public boolean containsAll(final Collection<?> collection) {
		for (final Object e : collection)
			if (!contains(e))
				return false;
		return true;
	}

	@Override
	public Object[] toArray() {
		return ArrayUtils.addAll(this.inherit.toArray(), super.toArray());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(final T[] a) {
		final int totalsize = size();
		final int mainsize = super.size();
		final int inheritsize = this.inherit.size();
		if (a.length<totalsize) {
			// Make a new array of a's runtime type, but my contents:
			final T[] elements = (T[]) Arrays.copyOf(this.inherit.toArray(), totalsize, a.getClass());
			System.arraycopy(super.toArray(), 0, elements, inheritsize, mainsize);
			return elements;
		}
		System.arraycopy(this.inherit.toArray(), 0, a, 0, inheritsize);
		System.arraycopy(super.toArray(), 0, a, inheritsize, mainsize);
		if (a.length>totalsize)
			a[totalsize] = null;
		return a;
	}

	@Override
	public Iterator<E> iterator() {
		return new InheritListIterator(this.main.iterator(), this.inherit.iterator());
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	class InheritListIterator extends ForwardingIterator<E> {
		private final Iterator<E> main;
		private final Iterator<E> inherit;

		public InheritListIterator(final Iterator<E> main, final Iterator<E> inherit) {
			this.main = main;
			this.inherit = inherit;
		}

		@Override
		protected Iterator<E> delegate() {
			return this.main;
		}

		@Override
		public boolean hasNext() {
			return this.inherit.hasNext()||super.hasNext();
		}

		private boolean isInherit;

		@Override
		public E next() {
			if (this.isInherit = this.inherit.hasNext())
				return this.inherit.next();
			else
				return super.next();
		}

		@Override
		public void remove() {
			if (this.isInherit)
				throw new UnsupportedOperationException("You can't modify inherited list.");
			else
				super.remove();
		}
	}

	class InheritListListIterator extends ForwardingListIterator<E> {
		private final ListIterator<E> main;
		private final ListIterator<E> inherit;

		public InheritListListIterator(final ListIterator<E> main, final ListIterator<E> inherit) {
			this.main = main;
			this.inherit = inherit;
		}

		@Override
		protected ListIterator<E> delegate() {
			return this.main;
		}

	}
}
