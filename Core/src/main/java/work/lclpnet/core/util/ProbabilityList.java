package work.lclpnet.core.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ProbabilityList<E> implements Iterable<E>{

	private final Map<E, Float> probabilityList = new HashMap<>();
	private float sum = 0F;
	private Random random;
	
	public ProbabilityList() {
		this(new Random());
	}
	
	public ProbabilityList(long seed) {
		this(new Random(seed));
	}
	
	public ProbabilityList(Random random) {
		this.random = random;
	}
	
	public void add(E elem, float probability) {
		add(elem, probability, true);
	}
	
	public void add(E elem, float probability, boolean duplicateAdditive) {
		if(elem == null || probability <= 0F) return;
		
		boolean duplicate = probabilityList.containsKey(elem);
		if(!duplicateAdditive && duplicate) return;
		
		sum += probability;
		
		if(duplicate) probability += probabilityList.get(elem);
		probabilityList.put(elem, probability);
	}
	
	public void remove(E elem) {
		if(elem == null || !probabilityList.containsKey(elem)) return;
		sum -= probabilityList.remove(elem);
	}
	
	public float getProbability(E elem) {
		if(elem == null || !probabilityList.containsKey(elem)) return 0F;
		return probabilityList.get(elem);
	}
	
	public float getSum() {
		return sum;
	}
	
	public void clear() {
		probabilityList.clear();
		sum = 0F;
	}
	
	public ProbabilityList<E> clone() {
		ProbabilityList<E> newObj = new ProbabilityList<>();
		
		for(E elem : probabilityList.keySet()) 
			newObj.add(elem, probabilityList.get(elem));
		
		return newObj;
	}
	
	public Set<E> getElements() {
		return probabilityList.keySet();
	}
	
	public E getRandomElement() {
		if(probabilityList.isEmpty()) return null;
		
		float randomNumber = random.nextFloat() * sum;

		for(E elem : probabilityList.keySet()) {
			float probability = probabilityList.get(elem);
			
			if(randomNumber < probability) return elem;
			else randomNumber -= probability;
		}

		throw new IllegalStateException("No element could be returned.");
	}

	@Override
	public Iterator<E> iterator() {
		return getElements().iterator();
	}
	
}
