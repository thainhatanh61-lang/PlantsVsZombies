public class PoleVaultingZombie extends Zombie {
    private boolean vaulted;

    public PoleVaultingZombie(int x, int y) {
        super(x, y, ZombieStats.POLE_VAULTING, 2);
    }

    @Override
    public void update(java.util.List<Plant> plants, java.util.List<Zombie> allZombies) {
        if (!vaulted) {
            for (Plant plant : plants) {
                if (!plant.isDead() && Math.abs(y - plant.getY()) <= 35 && Math.abs(x - plant.getX()) <= 35) {
                    x -= 90;
                    vaulted = true;
                    return;
                }
            }
        }
        super.update(plants, allZombies);
    }
}
