import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import java.text.SimpleDateFormat;

public class JavaGame1 extends JFrame implements ActionListener {

    private JTextArea textArea;
    private JButton Attack, HealthKit, Run, Reset, ShopButton;
    private JLabel yourText, enemyText, currencyLabel, levelLabel, experienceLabel, dateLabel;
    private JProgressBar currPHealth, currEHealth;

    private Random rand = new Random();
    private int health = 100;
    private int attackDamage = 40;
    private int healthKit = 3;
    private int healthKitBoost = rand.nextInt(21) + 20;
    private int experience = 0;
    private int level = 1;
    private int currency = 0;
    private int skillPoints = 0;
    private String[] enemies = { "Tai Lung", "Lord Shen", "Kai", "Tai Lung's Minions", "Lord Shen's Wolves",
            "Kai's Jade Zombies" };
    private int maxEnemyHealth = 85;
    private int enemyAttackDamage = 30;
    private int enemyHealth = rand.nextInt(maxEnemyHealth) + 1;
    private String enemy = enemies[rand.nextInt(enemies.length)];

    private JLabel attackerLabel;
    private JLabel enemyLabel;

    public JavaGame1() {
        super("Text-Based Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 500));

        textArea = new JTextArea(30, 30);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setEditable(false);

        JScrollPane js = new JScrollPane(textArea);
        js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        Attack = new JButton("Attack");
        HealthKit = new JButton("HealthKit");
        Run = new JButton("Run");
        Reset = new JButton("Reset");
        ShopButton = new JButton("Shop");

        Attack.addActionListener(this);
        HealthKit.addActionListener(this);
        Run.addActionListener(this);
        Reset.addActionListener(this);
        ShopButton.addActionListener(this);

        yourText = new JLabel("Your Health:");
        enemyText = new JLabel("Enemy Health:");
        currencyLabel = new JLabel("Currency: " + currency);
        levelLabel = new JLabel("Level: " + level);
        experienceLabel = new JLabel("Experience: " + experience);
        dateLabel = new JLabel();
        updateDateLabel();

        currPHealth = new JProgressBar(0, 100);
        currEHealth = new JProgressBar(0, 100);
        currPHealth.setStringPainted(true);
        currEHealth.setStringPainted(true);
        currPHealth.setForeground(Color.GREEN);
        currEHealth.setForeground(Color.RED);
        currPHealth.setValue(health);
        currEHealth.setValue(enemyHealth);

        attackerLabel = new JLabel(new ImageIcon("panda4.png")); // Player image
        enemyLabel = new JLabel(new ImageIcon("enemy3.png")); // Enemy image

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(attackerLabel, BorderLayout.WEST);
        imagePanel.add(enemyLabel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(Attack);
        buttonPanel.add(HealthKit);
        buttonPanel.add(Run);
        buttonPanel.add(Reset);
        buttonPanel.add(ShopButton);
        buttonPanel.add(yourText);
        buttonPanel.add(currPHealth);
        buttonPanel.add(enemyText);
        buttonPanel.add(currEHealth);
        buttonPanel.add(currencyLabel);
        buttonPanel.add(levelLabel);
        buttonPanel.add(experienceLabel);
        buttonPanel.add(dateLabel);

        add(imagePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(js, BorderLayout.CENTER);

        textArea.append("New Enemy Appeared: " + enemy + "\n");
        textArea.append("New Enemy Health: " + enemyHealth + "\n");

        pack();
        setVisible(true);
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = sdf.format(new Date());
        dateLabel.setText("Date: " + formattedDate);
    }

    public static void main(String[] args) {
        new JavaGame1();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Run) {

            Thread runThread = new Thread(() -> {
                int x = attackerLabel.getLocation().x;
                for (int i = 0; i < 40; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    x -= 6;
                    attackerLabel.setLocation(x, attackerLabel.getLocation().y);
                }

                resetGame();
            });

            runThread.start();
        } else if (e.getSource() == Attack) {
            handleAttack();
        } else if (e.getSource() == HealthKit) {
            useHealthKit();
        } else if (e.getSource() == Reset) {
            resetGame();
        } else if (e.getSource() == ShopButton) {
            handleShop();
        }

        currencyLabel.setText("Currency: " + currency);
        levelLabel.setText("Level: " + level);
        experienceLabel.setText("Experience: " + experience);
    }

    private void handleAttack() {
        int damageInflicted = rand.nextInt(attackDamage);
        int damageTaken = rand.nextInt(enemyAttackDamage);

        enemyHealth -= damageInflicted;
        health -= damageTaken;

        textArea.append("\n-> You attacked " + enemy + ", dealing " + damageInflicted + " damage.");
        textArea.append("\n-> You suffered " + damageTaken + " damage during combat.");

        if (health < 0)
            health = 0;
        if (health <= 20)
            currPHealth.setForeground(Color.RED);
        else
            currPHealth.setForeground(Color.GREEN);
        if (enemyHealth < 0)
            enemyHealth = 0;
        if (enemyHealth <= 20)
            currEHealth.setForeground(Color.GREEN);

        currPHealth.setValue(health);
        currEHealth.setValue(enemyHealth);

        if (enemyHealth <= 0) {
            handleEnemyDefeat();
        }

        if (health < 1) {
            handlePlayerDefeat();
        }
    }

    private void handleEnemyDefeat() {
        experience += 50;
        currency += 20 * level;

        textArea.append("\nYou defeated " + enemy + "!");
        textArea.append("\nYou gained 50 experience and " + (20 * level) + " currency.");

        if (experience >= 100) {
            level++;
            experience -= 100;
            textArea.append("\nCongratulations! You leveled up to level " + level + "!");
        }

        int choice = JOptionPane.showOptionDialog(
                null,
                "You win! Do you want to continue?",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[] { "Yes", "No" },
                "Yes");

        if (choice == JOptionPane.YES_OPTION) {
            resetEnemy();
        } else {
            JOptionPane.showMessageDialog(null, "Ending game");
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void handlePlayerDefeat() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "You lost! Reset the game?",
                "Confirm",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[] { "Yes", "No" },
                "Yes");

        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            JOptionPane.showMessageDialog(null, "Ending game");
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void useHealthKit() {
        if (healthKit > 0) {
            health += healthKitBoost;
            if (health > 100)
                health = 100;
            healthKit--;

            textArea.append("\nUsed a health kit and restored " + healthKitBoost + " health.");
            textArea.append("\nRemaining health kits: " + healthKit + "\n");

            currPHealth.setValue(health);

            JOptionPane.showMessageDialog(
                    this,
                    "Remaining health kits: " + healthKit,
                    "Health Kit Used",
                    JOptionPane.INFORMATION_MESSAGE);

            if (healthKit == 0) {
                JOptionPane.showMessageDialog(this, "No more health kits left.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "You don't have any health kits left!");
        }
    }

    private void handleShop() {
        int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to the shop! What would you like to buy?",
                "Shop",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] { "Health Kit", "Skill Point", "Exit" },
                "Health Kit");

        if (choice == JOptionPane.YES_OPTION) {
            int price = 50;
            if (currency >= price) {
                healthKit++;
                currency -= price;
                JOptionPane.showMessageDialog(this, "You bought a Health Kit!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough currency to buy a Health Kit.");
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            int price = 100;
            if (currency >= price) {
                skillPoints++;
                currency -= price;
                JOptionPane.showMessageDialog(this, "You bought a Skill Point!");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough currency for a Skill Point.");
            }
        }
    }

    private void resetGame() {
        textArea.setText("");
        health = 100;
        enemy = enemies[rand.nextInt(enemies.length)];
        enemyHealth = rand.nextInt(maxEnemyHealth) + 1;
        currPHealth.setValue(health);
        currEHealth.setValue(enemyHealth);
        currEHealth.setForeground(Color.RED);
        healthKit = 3;
        healthKitBoost = rand.nextInt(21) + 20;
    }

    private void resetEnemy() {
        enemy = enemies[rand.nextInt(enemies.length)];
        enemyHealth = rand.nextInt(maxEnemyHealth) + 1;
        currEHealth.setValue(enemyHealth);
        currEHealth.setForeground(Color.RED);

        textArea.append("New Enemy Appeared: " + enemy + "\n");
        textArea.append("New Enemy Health: " + enemyHealth + "\n");
    }
}
