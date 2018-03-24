package asw.inci_manager.inci_manager_gest.services;

import asw.inci_manager.inci_manager_gest.entities.Agent;
import asw.inci_manager.inci_manager_gest.entities.Incidence;
import asw.inci_manager.inci_manager_gest.repositories.IncidenceRepository;
import asw.inci_manager.kafka_manager.producers.KafkaProducer;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


@Service
public class IncidenceService {

	private static final Logger logger = Logger.getLogger(IncidenceService.class);
	
	@Autowired
    KafkaProducer kafkaProducer;

	@Autowired
	IncidenceRepository incidenceRepository;
	
	public void send(Incidence incidence)
	{
		// ToDO: Incorporar un campo topic dinámico o incluirlo como propertie:
		kafkaProducer.send("topic", new Gson().toJson(incidence));
		logger.info("Sending incidence \"" + incidence.getIncidenceName() + "\" to topic '" + "topic" +"'");
	}

	/**
	 * Devuelve las incidencias de un agente pasado por parámetro
	 * @param agent del que quieres obtener las incidencias
	 * @return lista de incidencias
	 */
	public Set<Incidence> getIncidencesFromAgent(Agent agent){
		return incidenceRepository.findIncidenceByAgent(agent);
	}
	
	/**
	 * Recibe una incidencia y la almacena en la base de datos
	 * @param incidence incidencia a guardar en la base de datos
	 */
	public void addIncidence(Incidence incidence) {
		incidenceRepository.save(incidence);
	}

	/**
	 * Retorna una incidencia buscando el id por parámetro
	 * @param id, incidencia a buscar
	 * @return la incidencia buscada
	 */
	public Incidence getIncidenceById(Long id){
		return incidenceRepository.findIncidenceById(id);
	}
	
	/**
	 * Recibe del formulario un String de incidencias separadas por comas y lo devuelve como un hashset de strings
	 * @param label String de incidencias separadas por comas
	 * @return HashSet de Strings
	 */
	public Set<String> labelsParser(String label) {
		String[] etiquetas = label.split(",");
		Set<String> labels = new HashSet<String>();
		for (String string : etiquetas) {
			labels.add(string);
		}
		return labels;
	}
}
