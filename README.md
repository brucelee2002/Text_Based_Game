# Text_Based_Game
JavaTGame is a text-based Java game featuring a Swing-based GUI. Players engage in battles against enemies, manage their health, utilize health kits, and shop for items. The game incorporates experience points and leveling up to enhance player progression. Designed for interactivity, it dynamically updates the game state based on player actions. 
# Features
Player and Enemy Mechanics:

Player starts with 100 health and can attack enemies, use health kits, run away, and reset the game.
Enemies have randomly generated health and attack damage.
User Interface:

The main interface consists of a text area displaying game messages, progress bars for player and enemy health, buttons for actions, and labels for various stats (currency, level, experience, and current date/time).

# Combat System:

Player can attack enemies, dealing random damage.
Enemies also attack the player, causing random damage.
Health kits can be used to restore health.

# Shop System:
Players can buy health kits and skill points using currency earned by defeating enemies.

# Leveling Up:

Players gain experience from defeating enemies.
Upon reaching 100 experience points, the player levels up, gaining additional benefits.

# Date and Time Display:

The current date and time are displayed and updated in the game interface.
Code Structure
1. Main Class and Setup
The main class JavaGame1 extends JFrame and implements ActionListener.
The constructor sets up the JFrame, initializes components, and displays the game window.
2. Action Handling
actionPerformed method handles different actions triggered by buttons (Attack, HealthKit, Run, Reset, Shop).
Each action is associated with specific game logic.
3. Combat Logic
Attack: Player and enemy both deal random damage to each other. Updates are reflected in the health bars and the text area.
HealthKit: Players can use health kits to restore health.
Run: Player can choose to run away, which resets the game.
Reset: Resets the game state.
Shop: Allows the player to buy health kits or skill points.
4. Game State Management
Methods like resetGame and resetEnemy are used to initialize or reset game states.
handleEnemyDefeat and handlePlayerDefeat manage what happens when an enemy or player is defeated.

Example Code Snippets
# Initialization and UI Setup:
<pre>
    <code class = "language-java">
public JavaGame1() {
    super("Text-Based Game");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setPreferredSize(new Dimension(900, 500));
    textArea = new JTextArea(30, 30);
    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    textArea.setEditable(false);
    
    JScrollPane js = new JScrollPane(textArea);
    js.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    // Initialize buttons and add action listeners
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
    
    // Set up labels and progress bars
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
</code>
</pre>    

# Action Handling:

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

# Attack Logic:
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

# Summary
JavaGame1 is a functional text-based game with a GUI, featuring a combat system, leveling, and a shop mechanism. The game is designed to be interactive, engaging the player with various actions and updating the game state dynamically based on user input. The project demonstrates core Java programming concepts, including GUI development with Swing, event handling, and game logic implementation.






