package mariculture.factory.blocks;


public class TileTurbineHand extends TileTurbineBase {		
	public int cooldown = 0;
    public int produced = 0;

	@Override
	public int getTankCapacity() {
		return 0;
	}
	
	@Override
	public int getRFCapacity() {
		return 1600;
	}

	@Override
	public int getEnergyGenerated() {
		return 2000;
	}

	@Override
	public int getEnergyTransferMax() {
		return 10;
	}

	@Override
	public boolean canOperate() {
		return true;
	}
	
	@Override
	public void updateEntity() {
		updateTurbine();
	}

	@Override
	public void addPower() {
		if(cooldown > 0) {
			cooldown--;
            produced++;
		}
		
		if(isCreatingPower && cooldown == 0) {
			isCreatingPower = false;
		}

        if(!isCreatingPower) {
            produced-=40;
        }
	}
}