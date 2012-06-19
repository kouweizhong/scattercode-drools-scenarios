package uk.co.scattercode.drools.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.drools.definition.rule.Rule;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A listener that will track all rule firings in a session.
 * 
 * @author Stephen Masters
 */
public class TrackingAgendaEventListener extends DefaultAgendaEventListener {

	private static Logger log = LoggerFactory.getLogger(TrackingAgendaEventListener.class);
	
	private List<Activation> activationList = new ArrayList<Activation>();

	@Override
	public void afterActivationFired(AfterActivationFiredEvent event) {
		Rule rule = event.getActivation().getRule();
		
		String ruleName = rule.getName();
		Map<String, Object> ruleMetaDataMap = rule.getMetaData();
		
		activationList.add(new Activation(ruleName));
		log.info("Rule fired: " + ruleName);
		
		StringBuilder sb = new StringBuilder("A total of [" + ruleMetaDataMap.size() + "] meta-data items were available for rule " + ruleName);
		for (String key : ruleMetaDataMap.keySet()) {
			sb.append("\nkey=" + key + ", value=" + ruleMetaDataMap.get(key));
		}
		log.info(sb.toString());
	}

	public boolean isRuleFired(String ruleName) {
		return activationList.contains(ruleName);
	}

	public void reset() {
		activationList.clear();
	}
	
	public final List<Activation> getActivationList() {
		return activationList;
	}
	
	public void logActivations() {
		if (activationList.size() == 0) {
			log.info("No activations occurred.");
		} else {
			StringBuilder sb = new StringBuilder("Activations: ");
			for (Activation activation : activationList) {
				sb.append("\n  rule: ").append(activation.getRuleName());
			} 
			log.info(sb.toString());
		}
	}

}