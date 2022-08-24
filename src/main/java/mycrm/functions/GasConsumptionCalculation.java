package mycrm.functions;

import java.util.List;

import mycrm.models.GasConsumption;

public class GasConsumptionCalculation {

	public Long calculateTotalGasConsumption(List<GasConsumption> gasConsumption) {

		Long total = 0L;
		for (GasConsumption g : gasConsumption) {
			
			total += g.getAnnualConsumption();
			
		}

		return total;

	}

}
