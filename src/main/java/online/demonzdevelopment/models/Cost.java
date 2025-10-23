package online.demonzdevelopment.models;

/**
 * Cost configuration model
 */
public class Cost {
    
    private double money;
    private double mobcoin;
    private double gem;
    private int xpLevels;
    private java.util.List<ItemCost> items;

    public Cost() {
        this.money = 0;
        this.mobcoin = 0;
        this.gem = 0;
        this.xpLevels = 0;
        this.items = new java.util.ArrayList<>();
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMobcoin() {
        return mobcoin;
    }

    public void setMobcoin(double mobcoin) {
        this.mobcoin = mobcoin;
    }

    public double getGem() {
        return gem;
    }

    public void setGem(double gem) {
        this.gem = gem;
    }

    public int getXpLevels() {
        return xpLevels;
    }

    public void setXpLevels(int xpLevels) {
        this.xpLevels = xpLevels;
    }

    public java.util.List<ItemCost> getItems() {
        return items;
    }

    public void setItems(java.util.List<ItemCost> items) {
        this.items = items;
    }

    public void addItem(ItemCost item) {
        this.items.add(item);
    }

    public boolean hasCost() {
        return money > 0 || mobcoin > 0 || gem > 0 || xpLevels > 0 || !items.isEmpty();
    }

    public void applyMultiplier(double multiplier) {
        this.money *= multiplier;
        this.mobcoin *= multiplier;
        this.gem *= multiplier;
        this.xpLevels = (int) (this.xpLevels * multiplier);
        for (ItemCost item : items) {
            item.setAmount((int) (item.getAmount() * multiplier));
        }
    }

    /**
     * Item cost sub-model
     */
    public static class ItemCost {
        private String material;
        private int amount;

        public ItemCost(String material, int amount) {
            this.material = material;
            this.amount = amount;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
