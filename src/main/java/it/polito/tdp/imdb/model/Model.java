package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	Map<Integer, Director> idMapDirector;
	ImdbDAO dao;
	Graph<Director, DefaultWeightedEdge> grafo;
	List<Vicino> percorso;
	List<Director> percorsoMigliore;
	
	public Model () {
		idMapDirector = new HashMap<> ();
		dao = new ImdbDAO();
		dao.listAllDirectors(idMapDirector);
		percorsoMigliore = new ArrayList<>();
	}
	
	public void creaGrafo(int anno) {
		grafo = new SimpleWeightedGraph<Director, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		
		// vertici
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(idMapDirector, anno));
		
		// archi
		
		for (Adiacenza a: dao.getArchi(idMapDirector, anno)) {
			if (grafo.getEdge(a.d1, a.d2) == null || grafo.getEdge(a.d2, a.d1) == null) {
				Graphs.addEdgeWithVertices(grafo, a.d1, a.d2, a.peso);
			}
		}
		
	}
	
	public int numVertici() {
		return grafo.vertexSet().size();
	}

	public int numArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<Director> getDirettori() {
		List<Director> lista = new ArrayList<Director> ();
		
		for (Director d: grafo.vertexSet()) {
			lista.add(d);
		}
		
		return lista;
	}
	
	public ArrayList<Vicino> vicini (Director d, int anno) {
		ArrayList<Vicino> lista = new ArrayList<Vicino> ();
		
		for (Adiacenza a: dao.getArchi(idMapDirector, anno)) {
			if (a.d1.equals(d)) {
				Vicino v = new Vicino(a.getD2(), a.getPeso());
				lista.add(v);
			} else if (a.d2.equals(d)) {
				Vicino v = new Vicino(a.getD1(), a.getPeso());
				lista.add(v);
			}
		}
		
		Collections.sort(lista);
		
		return lista;
	}
	
	int conta = 0;
	public List<Vicino> trovaPercorso(Vicino inizio, int numeroMax, int anno){
		this.percorso = new ArrayList<>();
		List<Vicino> parziale = new ArrayList<>();
		parziale.add(inizio);
		cerca(numeroMax,  parziale, anno);
		return parziale;
	}
	
	private void cerca(int numeroMax, List<Vicino> parziale, int anno) {
		// caso terminale 
		if (conta == numeroMax) {
			List<Vicino> parzMenoUno = new ArrayList<>(parziale);
			parzMenoUno.remove(parziale.size());
			if (pTot(parziale) > pTot(parzMenoUno)) {
				this.percorso.clear();
				for (Vicino v: parziale) {
					this.percorso.add(v);
				}
			}
			return;
		}
		
		// ...altrimenti, scorro i vicini dell'ultimo inserito, e provo
		// ad aggiungerli uno ad uno
		for(Director d : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1).d)) {
			List<Vicino> vicinidiD = vicini (d, anno);
			if(!vicinidiD.contains(d)) {
				parziale.add(vicinidiD.get(0));
				cerca(numeroMax, parziale, anno);
				parziale.remove(parziale.size() - 1);
			}
		}
		
		
	}
	
	public int pTot(List<Vicino> lista) {
		int i = 0;
		for (Vicino v: lista) {
			i = i + v.getP();
		}
		return i;
	}
	
	
	public List<Director> trovaPercorso1(Director partenza,int maxAttori){
		percorsoMigliore=new ArrayList<>();
		List<Director> parziale=new ArrayList<>();
		parziale.add(partenza);
		cerca1(maxAttori,parziale,0);
		return percorsoMigliore;
		}
		                    //metodo privato
		private void cerca1(int maxAttori, List<Director> parziale,int attori) {
		//caso terminale
		if(attori>maxAttori) {
			if(parziale.size() > this.percorsoMigliore.size()) {
				this.percorsoMigliore = new ArrayList<> (parziale);
			}
			return;
		}
		// ...altrimenti, scorro i vicini dell'ultimo inserito, e provo
		// ad aggiungerli uno ad uno
		for(Director vicino : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
				if(!parziale.contains(vicino)) {
					parziale.add(vicino);
					attori=attori+calcolaAttori((parziale.get(parziale.size()-1)),vicino);
					cerca1(maxAttori, parziale,attori);
					parziale.remove(parziale.size() - 1);
					attori=attori-calcolaAttori((parziale.get(parziale.size()-1)),vicino);
				}
			}
		}

		private int calcolaAttori(Director d1, Director d2) {
			if(grafo==null)
				return 0;
			
		return (int) grafo.getEdgeWeight(grafo.getEdge(d1, d2));
		}

}
