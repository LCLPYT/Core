package work.lclpnet.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListUtils {

	public static List<?> randomizeList(List<?> list) {
		Collections.shuffle(list);
		return list;
	}

	public static List<?> setToList(Set<?> set) {
		List<Object> list = new ArrayList<>();
		for(Object o : set) {
			list.add(o);
		}
		return list;
	}

	public static boolean containsStringIgnoreCase(List<String> list, String s) {
		boolean contains = false;
		for(String values : list) {
			if(values.equalsIgnoreCase(s)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	public static Map<String, Integer> sortDescending(Map<String, Integer> from) {
		int highest = 0;
		List<String> order = new ArrayList<>();
		for(String s : from.keySet()) {
			int i = from.get(s);
			if(i >= highest) {
				highest = i;
				order.add(s);
			}
		}
		Collections.reverse(order);
		Map<String, Integer> sorted = new HashMap<>();
		for(String s : order) sorted.put(s, from.get(s));
		return sorted;
	}

	public static int quantityOf(List<?> list, Object obj) {
		if(list == null) return 0;
		if(obj == null) return 0;

		int count = 0;
		for(Object o : list) if(o.equals(obj)) count++;

		return count;
	}

	public static int quantityOfObject(List<Object> list, Object obj) {
		if(list == null) return 0;
		if(obj == null) return 0;

		int count = 0;
		for(Object o : list) if(o.equals(obj)) count++;

		return count;
	}

	public static boolean isDuplicate(List<?> list, Object obj) {
		return quantityOf(list, obj) > 1;
	}

	public static boolean isDuplicateObject(List<Object> list, Object obj) {
		return quantityOfObject(list, obj) > 1;
	}

	public static <T> List<T> getRemovedDuplicatesList(List<T> list) {
		List<T> verified = new ArrayList<>();
		for(T o : list) if(!verified.contains(o)) verified.add(o);
		return verified;
	}

	public static <T> void removeDuplicates(List<T> list) {
		List<T> verified = new ArrayList<>();
		List<T> removal = new ArrayList<>();
		
		for(T o : list) {
			if(verified.contains(o)) removal.add(o);
			else verified.add(o);
		}
		
		for(T t : removal) list.remove(t);
	}
	
	public static boolean hasEqualEntries(List<?> first, List<?> second) {
		if(first == null || second == null) return false;
		for(Object o : first) if(!second.contains(o)) return false;
		for(Object o : second) if(!first.contains(o)) return false;
		return true;
	}

	public static <T> T getRandomElement(List<T> list) {
		if(list == null || list.isEmpty()) return null;
		return list.get(Randoms.mainRandom.nextInt(list.size()));
	}

	@SafeVarargs
	public static <T> T getRandomElement(T... array) {
		if(array == null || array.length <= 0) return null;
		return array[Randoms.mainRandom.nextInt(array.length)];
	}

}