package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private ImdbDAO dao;
	private Map<Integer, Actor> actorIdMap;
	private List<Arco> archi;
	
	public Model() {
		
		dao = new ImdbDAO();
	}
	
	public List<String> listaGeneri() {
		
		return this.dao.listaGeneri();
	}
	
	public void creaGrafo(String genere) {
		
		actorIdMap = new HashMap<Integer, Actor>();
		grafo = new SimpleWeightedGraph<Actor, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		archi = new ArrayList<Arco>();
		
		this.dao.creaVertici(actorIdMap, genere);
		
		Graphs.addAllVertices(this.grafo, actorIdMap.values());
		
		archi = this.dao.listaArchi(actorIdMap, genere);
		
		for (Arco ar : archi) {
			Graphs.addEdgeWithVertices(this.grafo, ar.getA1(), ar.getA2(), ar.getPeso());
		}
	}
	
	public List<String> listaAttori() {
		
		List<String> result = new ArrayList<String>();
		for (Actor a : actorIdMap.values()) {
			result.add(a.toString());
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public String attoriSimili(String s) {
		
		String result = "";
		List<Actor> attori = new ArrayList<Actor>();
		
		for (Actor a : actorIdMap.values()) {
			if (a.toString().equals(s)) {
			ConnectivityInspector<Actor, DefaultWeightedEdge> inspector =
			new ConnectivityInspector<Actor, DefaultWeightedEdge>(this.grafo);
			
			Set<Actor> set = inspector.connectedSetOf(a);
			set.remove(a);
			
			for (Actor aa : set) {
				attori.add(aa);
			}
			
			Collections.sort(attori, new Comparator<Actor>() {
				 @Override
				 public int compare(Actor o1, Actor o2)
				 {
				 return o1.getLastName().compareTo(o2.getLastName());
				 }});
			
			for (Actor aaa : attori) {
				result = result + aaa.toString() + "\n";
			}
						
			}
		}
		
		return result;
		
	}

	public int numeroVertici() {
		return this.grafo.vertexSet().size();
		}
		 public int numeroArchi() {
		return this.grafo.edgeSet().size();
		}


}
