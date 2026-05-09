public class NewspaperZombie extends Zombie {
    public NewspaperZombie(int x, int y) {
        super(x, y, ZombieStats.NEWSPAPER, 1);
    }

    @Override
    public void update(java.util.List<Plant> plants, java.util.List<Zombie> allZombies) {
        if (health <= ZombieStats.NEWSPAPER_BODY && health > 0) {
            speed = 2;
        }
        super.update(plants, allZombies);
    }
}
