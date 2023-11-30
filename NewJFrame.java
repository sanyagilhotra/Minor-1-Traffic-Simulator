import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
//import java.awt.Polygon;
public class NewJFrame extends javax.swing.JFrame {
    /**
     * Creates new form NewJFrame
     */
    ImageIcon iconLogo = new ImageIcon("Background.png");//For the welcome frame
    public NewJFrame() {
        initComponents();
    }
class Driver extends Thread //This is the main class with the main frame
{   int p[][]=new int[220][120]; //Grid used to describe the map
    Car[] carObj =new Car[50];//Creating Instances of cars
    int traffic_light=0;//Variable for switching traffic light
    int traffic_interval=0;//Multiplier for finding out time to switch the traffic light
    int spawn_path=0;       //The variable used to calculate lane used by car
    int spawn_area[]=new int[5];// Array to find if the spawn area of a lane is occupied by a car or not
    
    Random randnum=new Random(System.currentTimeMillis());// To generate random numbers for various purposes
    int car_no=0;        //This number will determine how many cars would show up at max on the map
    int spawn_interval=0; //Determines the time interval between new car spawns
    int sim_status=0;           //If the simulation has to stop or not
    JButton stop_sim,time_inter1,time_inter2,time_inter3,cars_bucket1,cars_bucket2,cars_bucket3,spawn_inter1,spawn_inter2,spawn_inter3,toggle_lights,clear_traffic; //Jbutton declarations
    JLabel light_intervalL,cars_on_mapL,spawnL,total_carsL,ts,cs,ss;                  //JLabel declarations
    int traff_mul=1000,spawn_mul=50,cars_allow=15; //Initializing traffic and spawn multipliers after which an action takes place
    double spawn_sec=((double)(spawn_mul*5))/1000; //To convert spawn multiplier into seconds for display purpose

    JFrame frame;
    Driver() //Constructor for Driver class
    { 
        for(int i=0;i<50;i++)
        carObj[i]=new Car();     //Initializing all cars
        for(int i=0;i<220;i++)  //Setting all co-ordinates on the map as unoccupied
        {
            for(int j=0;j<120;j++) //Array starts at index 0 in both x-y plane, but co-ordinates range from x=-100 to 1920 and y=-100 to 1080
            {                       //Each index contains a square of 10 pixels wide
                p[i][j]=0;          // To calculate the index used by a particular point,add 100 to both x and y and divide by 10
            }  
        }
        spawn_sec=spawn_sec/10;
        sim_status=1;
        stop_sim=new JButton();                                                //Initializing buttons and labels for display
        time_inter1=new JButton();time_inter2=new JButton();time_inter3=new JButton();
        cars_bucket1=new JButton();cars_bucket2=new JButton();cars_bucket3=new JButton();
        spawn_inter1=new JButton();spawn_inter2=new JButton();spawn_inter3=new JButton();
        toggle_lights=new JButton();
        clear_traffic=new JButton();
        light_intervalL=new JLabel();
        cars_on_mapL=new JLabel();
        spawnL=new JLabel();
        total_carsL=new JLabel();
        ts=new JLabel();
        cs=new JLabel();
        ss=new JLabel();
        frame=new JFrame(); //Frame basic characterstics
        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setLayout(new java.awt.BorderLayout());           //j is the frame which holds the panel
        frame.setSize(1920,1080);
        MyPanel myPanelObj=new MyPanel();  //Panel which is used to paint the simulation
        myPanelObj.setLayout(null);
        stop_sim.setBounds(1580,50,120,50);
        time_inter1.setBounds(1300,120,100,50);
        time_inter2.setBounds(1450,120,100,50);
        time_inter3.setBounds(1600,120,100,50);
        cars_bucket1.setBounds(1300,190,100,50);
        cars_bucket2.setBounds(1450,190,100,50);
        cars_bucket3.setBounds(1600,190,100,50);
        spawn_inter1.setBounds(1300,260,100,50);
        spawn_inter2.setBounds(1450,260,100,50);
        spawn_inter3.setBounds(1600,260,100,50);
        toggle_lights.setBounds(1437,50,120,50);
        clear_traffic.setBounds(1290,50,120,50);
        light_intervalL.setBounds(1100,120,200,50);
        cars_on_mapL.setBounds(1130,190,200,50);
        spawnL.setBounds(1120,260,200,50);
        total_carsL.setBounds(1130,50,200,50);
        ts.setBounds(1750,120,100,50);
        cs.setBounds(1750,190,100,50);
        ss.setBounds(1750,260,100,50);
        total_carsL.setFont(new Font("Serif",Font.PLAIN,20));
        ts.setFont(new Font("Serif",Font.PLAIN,20));
        cs.setFont(new Font("Serif",Font.PLAIN,20));
        ss.setFont(new Font("Serif",Font.PLAIN,20));
        spawnL.setFont(new Font("Serif",Font.PLAIN,20));
        cars_on_mapL.setFont(new Font("Serif",Font.PLAIN,20));
        light_intervalL.setFont(new Font("Serif", Font.PLAIN, 20));
        light_intervalL.setText("Traffic Light Interval");
        cars_on_mapL.setText("Cars on Map");
        spawnL.setText("Spawn Interval");
        total_carsL.setText("Total Cars:"+car_no);
        ts.setText(Integer.toString((traff_mul*5)/1000));
        cs.setText(Integer.toString(cars_allow));
        ss.setText(Double.toString(spawn_sec));
        stop_sim.setText("Stop Simulation");
        time_inter1.setText("5 Seconds");
        time_inter2.setText("10 Seconds");
        time_inter3.setText("15 Seconds");
        cars_bucket1.setText("10 Cars");
        cars_bucket2.setText("20 Cars");
        cars_bucket3.setText("30 Cars");
        spawn_inter1.setText("0.25 Second");
        spawn_inter2.setText("0.5 Second");
        spawn_inter3.setText("1 Second");
        
        toggle_lights.setText("Toggle Lights");
        clear_traffic.setText("Clear Traffic");
        myPanelObj.add(time_inter1);myPanelObj.add(time_inter2);myPanelObj.add(time_inter3);
        myPanelObj.add(cars_bucket1);myPanelObj.add(cars_bucket2);myPanelObj.add(cars_bucket3);
        myPanelObj.add(spawn_inter1);myPanelObj.add(spawn_inter2);myPanelObj.add(spawn_inter3);
        myPanelObj.add(stop_sim);
        myPanelObj.add(toggle_lights);
        myPanelObj.add(clear_traffic);
        myPanelObj.add(light_intervalL);
        myPanelObj.add(cars_on_mapL);
        myPanelObj.add(spawnL);
        myPanelObj.add(total_carsL);
        myPanelObj.add(ts);
        myPanelObj.add(cs);
        myPanelObj.add(ss);

        frame.add(myPanelObj,BorderLayout.CENTER); 
        //The following block of codes are just for adding functinality to the buttons
        clear_traffic.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    clearTrafficActionPerformed(evt);
                }
        
        });
        stop_sim.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    stopSimActionPerformed(evt);
                }
        
        });
        time_inter1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    timeInter1ActionPerformed(evt);
                }
        
        });
        time_inter2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    timeInter2ActionPerformed(evt);
                }
        
        });
        time_inter3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    timeInter3ActionPerformed(evt);
                }
        
        });
        cars_bucket1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    carsBucket1ActionPerformed(evt);
                }
        
        });
        cars_bucket2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    carsBucket2ActionPerformed(evt);
                }
        
        });
        cars_bucket3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    carsBucket3ActionPerformed(evt);
                }
        
        });
        spawn_inter1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    spawnInter1ActionPerformed(evt);
                }
        
        });
        spawn_inter2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    spawnInter2ActionPerformed(evt);
                }
        
        });
        spawn_inter3.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    spawnInter3ActionPerformed(evt);
                }
        
        });
        toggle_lights.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    toggleLightsActionPerformed(evt);
                }
        
        });
    }   
    private void clearTrafficActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        for(int i=0;i<50;i++)
            carObj[i].spawn=0;

        for(int i=0;i<220;i++){
                for(int j=0;j<120;j++){
                    p[i][j]=0;
                }
            }
        car_no=0;
    }
    
    private void stopSimActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        this.frame.dispose();
        sim_status=0;
       
    }
    private void timeInter1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        traff_mul=1000;
         
    }
    private void timeInter2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        traff_mul=2000;
          
    }
    private void timeInter3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        traff_mul=3000;
        
    }
    private void carsBucket1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        cars_allow=10;
     
    }
    private void carsBucket2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        cars_allow=20;
       
    }
    private void carsBucket3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        cars_allow=30;
       
    }
    private void spawnInter1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        spawn_mul=50;
        
    }
    private void spawnInter2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        spawn_mul=100;
       
    }
    private void spawnInter3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
        spawn_mul=200;
       
    }
    private void toggleLightsActionPerformed(java.awt.event.ActionEvent evt) { //Switching the traffic lights manually                                        
        // TODO add your handling code here:
        if(traffic_light==0)
            traffic_light=1;
        else if(traffic_light==1)
            traffic_light=2;
        else
            traffic_light=0;

    }
  
    @Override
    
    /*
    Information about road dimensions for future use
    Horizontal road is drawn between x=0 to x=1920 and y=500 to y=800
    Vertical road is drawn between x=900 to x=1050 and y=0 to y=725
    Length of car=100 pixels
    Width of car=42 pixels
    Width of one lane=75 pixels
    */
    public void run() //This thread calculates the position of cars at every instant
    {
        try
        { while(sim_status==1)//Traffic lights controller
            {   if(traffic_interval>traff_mul){ //Condition to flip the traffic light
                traffic_interval=0;
                if(traffic_light==0)        //If traffic light is zero, only east to west traffic is allowed 
                     traffic_light=1;        //If traffic light is 1, only west to east traffic is allowed
                else if(traffic_light==1)   //If traffic light is 2, only north to south traffic is allowed
                    traffic_light=2;
                else
                    traffic_light=0;
                }
                if(spawn_interval>=spawn_mul && car_no<cars_allow) //This function spawns a new car after a specific time interval
                {   spawn_interval=0;
                    for(int k=0;k<5;k++)
                        spawn_area[k]=0;
                        
                    spawn_path=randnum.nextInt(5); //Selects the lane chosen by car //qwert-change to 8
                            
                    for(int i=0;i<50;i++)// Checking if there is any car in the spawn area
                    {
                        if(carObj[i].spawn==1 && carObj[i].path==spawn_path) //This function checks all cars and sees if there is any car int the spawn area of the selected lane
                        {
                            if(spawn_path==0 || spawn_path==1) //If selected lane is east to west ones
                            {
                                if(carObj[i].curr_x<20)
                                {   
                                    spawn_area[spawn_path]=1;
                                    break;
                                }
                                else
                                    spawn_area[spawn_path]=0;
                            }
                            else if(spawn_path==2 || spawn_path==3){ //If selected lane is west to east
                                if(carObj[i].curr_x>1800){   
                                    spawn_area[spawn_path]=1;
                                    break;
                                }
                                else
                                    spawn_area[spawn_path]=0;        
                            }
                            else if(spawn_path==4 && carObj[i].path==4){ //If selected lane is north to south
                            
                                if(carObj[i].curr_y<20){
                                    spawn_area[spawn_path]=1;
                                    break;
                                }
                                else
                                    spawn_area[spawn_path]=0;
                            }
                        } 
                    }
                    for(int i=0;i<50;i++){ //This function finds the car in the car array which hasn't spawn yet
                                if(carObj[i].spawn==0) //If car hasn't spawn, then enter
                            {    
                            
                            if(spawn_area[spawn_path]==0) //If spawn_area is zero, it means the car can spawn on the selected lane, otherwise not
                            {
                                    carObj[i].dest_sel=randnum.nextInt(2); //Destination selecter
                            if(spawn_path==0 || spawn_path==1)//If source is East, destination can be West or North
                            {   if(carObj[i].dest_sel==0)
                                    carObj[i].destination="W";
                                else
                                    carObj[i].destination="N";
                            }
                            else if(spawn_path==2 || spawn_path==3)//If source is West, destination can be East or North
                            {
                                if(carObj[i].dest_sel==0)
                                    carObj[i].destination="E";
                                else
                                    carObj[i].destination="N";
                            }
                            else                                    //If source is North, destination can be West or East
                            {   if(carObj[i].dest_sel==0)
                                    carObj[i].destination="E";
                                else
                                    carObj[i].destination="W";
                            }
                                
                            ++car_no;                               //Increment car no
                            carObj[i].spawn=1;                           //Set spawn value of car to 1
                            carObj[i].path=spawn_path;                   //Set lane of the car
                            carObj[i].colour=randnum.nextInt(10);        //Set color of the car according to random number
                            carObj[i].speed=2+randnum.nextInt(5);        //Select speed of car according to random number
                            carObj[i].set();                             //Call car class's method to set values of variables of car object
                            if(carObj[i].path<4)                         //To mark the position taken by fresh spawn car as occupied
                            {
                            for(int k=0;k<10;k++)
                        {
                            for(int j=0;j<4;j++)
                                p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=1;//If p[i][j]=1, this means the block determined by p[i][j] is occupied
                        }
                            }
                            else
                            {
                            for(int k=0;k<10;k++)
                        {
                            for(int j=0;j<4;j++)
                                p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=1;
                        }  
                            }
                            
                            
                            }
                            
                            break;
                        }
                    }   
                        
                }
                
                for(int i=0;i<50;i++) //This function determines the motion of car on the map
                {
                    carObj[i].moving=1;
                
                
                
                    if(carObj[i].spawn==1 && ((carObj[i].path==0)||carObj[i].path==1)) //Road going East to West
                    {   if(traffic_light!=0)        //If value of traffic light is not 0, this means the cars need to stop on these lanes
                        {
                            if(carObj[i].curr_x+100>890 && carObj[i].curr_x+100<900)// To stop the car at the intersection
                                carObj[i].moving=0;
                            if(carObj[i].curr_x+100>320 && carObj[i].curr_x+100<330)
                            {
                                carObj[i].moving=0;
                            }
                        }
                        else
                            carObj[i].moving=1; 
                        
                        
                        for(int k=1;k<3;k++)        //Checking if there is any car in front
                        {
                        if(p[(carObj[i].curr_x+200)/10+k][(carObj[i].curr_y+100)/10]!=0 )
                        {   carObj[i].moving=0; //When moving is zero, car is stopped
                            break;
                        }
                        }
                    
                        
                        
                    
                        if(carObj[i].moving==1)
                        {
                            if(carObj[i].turn==1 && carObj[i].curr_x>860) //For turning the car left
                            {    
                                int allow=0;
                                outer:for(int k=0;k<7;k++) // This loop finds if there is any car or possible collision while turning
                                {
                                    for(int j=1;j<9;j++)
                                    {
                                        if(p[98+k][(carObj[i].curr_y+100)/10-j]==1)
                                        {
                                            allow=0;
                                            break outer;
                                        }
                                        else
                                            allow=1;
                                            
                                    }
                                }   
                                if(allow==1) //If allow=1, this means the way is clear and car can turn
                                {
                                    for(int k=0;k<10;k++) //Updating the grid with car's new position
                                    {     for(int j=0;j<4;j++)
                                            p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                                    
                                    }
                                    carObj[i].path=5; //New lane
                                    carObj[i].horizontal=0;
                                    carObj[i].vertical=1;      
                                    carObj[i].curr_x=920;       //New fixed x co-ordinate
                                    carObj[i].curr_y=carObj[i].curr_y-60; //Changing y coordinate
                                    carObj[i].turn=0;
                                    for(int k=0;k<10;k++)  //Updating the newly occupied position
                                    {
                                        for(int j=0;j<4;j++)
                                            p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=1;
                                    }
                                    
                                    continue; //Skips the rest of the code as the lane has changed
                                    
                                }
                                else
                                    continue;//If allow not equal to 1, car waits until the way is clear
                            
                            }
                            

                            for(int k=0;k<10;k++) //Updating the grid with car's new position
                                {   for(int j=0;j<4;j++)
                                        p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                                
                                }
                            carObj[i].curr_x+=carObj[i].speed; //Changing the postion of car
                            
                            for(int k=0;k<10;k++) //Updating the occupied space
                                {
                                    for(int j=0;j<4;j++)
                                        p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=1;
                            }
                        }
                    
                        if(carObj[i].curr_x>1920) //Despawning the car after it has gone out of map
                        {
                            carObj[i].spawn=0;
                            --car_no;
                            for(int k=0;k<10;k++) 
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                            }
                        }
                    
                    }
                
                    if(carObj[i].spawn==1 && (carObj[i].path==2 || carObj[i].path==3)) //Road going West to East
                    {
                        if(carObj[i].curr_x<=-60) //Despawning the car after it has gone out of map
                        {
                            carObj[i].spawn=0;
                            --car_no;
                            for(int k=0;k<10;k++) 
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                            }
                            continue;
                        }
                        if(traffic_light!=1)   //If traffic light is not one, the traffic with source west is not allowed
                            {
                                if(carObj[i].curr_x<1090 && carObj[i].curr_x>1050)
                                    carObj[i].moving=0;
                                if(carObj[i].curr_x<510 && carObj[i].curr_x>500)
                                    carObj[i].moving=0;
                            }
                            else
                                carObj[i].moving=1;
                        
                        for(int k=2;k<5;k++)
                            if(p[(carObj[i].curr_x+100)/10-k][(carObj[i].curr_y+100)/10]!=0 )//Checking if there is any car in front
                            {   carObj[i].moving=0; //When moving is zero, car is stopped
                                break;
                            }
                        
                        if(carObj[i].moving==1)
                        {
                            if(carObj[i].turn==2 && carObj[i].curr_x<930) //For turning the car
                            {   int allow=0;
                                outer:for(int k=2;k<10;k++)   //Same function as above, only the relative coordinates and math has changed
                                {
                                    for(int j=1;j<9;j++)
                                    {
                                        if(p[100+k][(carObj[i].curr_y+100)/10-j]==1)
                                        {
                                            allow=0;
                                            break outer;
                                        }
                                        else
                                            allow=1;
                                            
                                    }
                                }
                                if(allow==1)  //Car can turn
                                {
                                    for(int k=0;k<10;k++) //Updating the grid with car's new position
                                    {   for(int j=0;j<4;j++)
                                        p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                                
                                    }
                                    carObj[i].path=5;
                                    carObj[i].horizontal=0;
                                    carObj[i].vertical=1;
                                    carObj[i].curr_x=920;
                                    carObj[i].curr_y=carObj[i].curr_y-60;
                                    carObj[i].turn=0;
                                    for(int k=0;k<10;k++)
                                    {
                                    for(int j=0;j<4;j++)
                                        p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=1;
                                    }
                                    
                                    continue;
                                    
                                }
                                else
                                    continue;        
                            }
                            for(int k=0;k<10;k++) //Updating the grid with car's new position
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=0;
                            }
                            carObj[i].curr_x-=carObj[i].speed; //Changing position of car
                            
                            for(int k=0;k<10;k++)
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=1;
                            }
                    
                        }
                    }
                    if(carObj[i].spawn==1 && carObj[i].path==4) //Car going from North to Intersection
                    {   if(traffic_light!=2) //Same thing as above
                        {
                            if(carObj[i].curr_y+100>390 && carObj[i].curr_y+100<500)
                                carObj[i].moving=0;
                        }
                        else
                            carObj[i].moving=1;
                        for(int k=1;k<3;k++)      //Checking the traffic ahead
                        {
                            if(p[(carObj[i].curr_x+100)/10][(carObj[i].curr_y+200)/10+k]!=0 )//Checking if there is any car in front
                            {   carObj[i].moving=0; //When moving is zero, car is stopped
                                break;
                            }
                        }
                        
                        if(carObj[i].moving==1)
                        {
                        if(carObj[i].turn==1 && carObj[i].curr_y>450) //For turning the car left
                        {    int allow=1;
                            for(int k=1;k<11;k++) //Checking if way is clear
                            {
                                if(p[(carObj[i].curr_x+140)/10+k][62]==1)
                                { allow=1;
                                    break;
                                }
                            }
                            if(allow==1)
                            {
                                for(int k=0;k<10;k++) //Updating the grid with car's new position
                            {   for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=0;
                            
                            }
                                carObj[i].path=0;
                                carObj[i].horizontal=1;
                                carObj[i].vertical=0;
                                carObj[i].curr_x=carObj[i].curr_x;
                                carObj[i].curr_y=420;
                                carObj[i].turn=0;
                                for(int k=0;k<10;k++)
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=1;
                            }
                                
                                continue;
                                
                            }
                            else
                                continue;
                                
                            }
                        if(carObj[i].turn==2 && carObj[i].curr_y>500) //For turning the car right
                        {    int allow=1;
                            for(int k=2;k<10;k++)
                            {
                                if(p[(carObj[i].curr_x+100)/10-k][76]==1)
                                { allow=0;
                                    break;
                                }
                            }
                            if(allow==1)
                            {
                                for(int k=0;k<10;k++) //Updating the grid with car's new position
                            {   for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=0;
                            
                            }
                                carObj[i].path=2;
                                carObj[i].horizontal=1;
                                carObj[i].vertical=0;
                                carObj[i].curr_x=carObj[i].curr_x-60;
                                carObj[i].curr_y=840;
                                carObj[i].turn=0;
                                for(int k=0;k<10;k++)
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+k][(carObj[i].curr_y+100)/10+j]=1;
                            }
                                
                                continue;
                                
                            }
                            else
                                continue;
                                
                            }
                        for(int k=0;k<10;k++) //Updating the grid with car's new position
                            {   for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=0;
                            
                            }
                        carObj[i].curr_y+=carObj[i].speed;
                        
                        for(int k=0;k<10;k++)
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=1;
                            }
                        }
                    }
                    if(carObj[i].spawn==1 && carObj[i].path==5)// Cars going from intersection to North
                    {
                        if(carObj[i].curr_y<=-60) //Despawning the car after it has gone out of map
                        {
                            carObj[i].spawn=0;
                            --car_no;
                            for(int k=0;k<10;k++) 
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=0;
                            }
                            continue;
                        }
                        for(int k=2;k<5;k++)
                            if(p[(carObj[i].curr_x+100)/10][(carObj[i].curr_y+100)/10-k]!=0 )//Checking if there is any car in front
                            {   carObj[i].moving=0; //When moving is zero, car is stopped
                                break;
                            }
                        
                        if(carObj[i].moving==1)
                        {
                            for(int k=0;k<10;k++) //Updating the grid with car's new position
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=0;
                            }
                            carObj[i].curr_y-=carObj[i].speed;
                            
                            for(int k=0;k<10;k++)
                            {
                                for(int j=0;j<4;j++)
                                    p[(carObj[i].curr_x+100)/10+j][(carObj[i].curr_y+100)/10+k]=1;
                            }
                        }
                    }
                }
                frame.repaint();    //Paints the roads and cars on the map
                ++spawn_interval; 
                ++traffic_interval;
                total_carsL.setText("Total Cars:"+(int)Math.ceil(car_no*1.2));
                spawn_sec=((double)(spawn_mul*5))/1000;
                
                ts.setText(Integer.toString((traff_mul*5)/1000));
                cs.setText(Integer.toString(cars_allow));
                ss.setText(Double.toString(spawn_sec));
                Thread.sleep(5);   //Thread sleeps for 5 milliseconds
            
            }

        }
        catch(Exception e)
        {

        }
    }
   

        
   

    class MyPanel extends JPanel //This class contains the paint method
    {  
                @Override
        public void paintComponent(Graphics g)
        {
            Graphics2D g2=(Graphics2D)g;
            Rectangle2D hroad = new Rectangle2D.Float();//For creating horizontal road
            //Polygon EW=new Polygon(new int[]{700,800,800,850,800,800,765,765,775,750,725,735,735,700},new int[]{440,440,450,425,400,410,410,390,390,340,390,390,410,410},14); //For painting traffic signals
            //Polygon WE=new Polygon(new int[]{1150,1150,1250,1250,1215,1215,1225,1200,1175,1185,1185,1150,1150,1100},new int[]{950,940,940,910,910,890,890,840,890,890,910,910,900,925},14);
            //Polygon N=new Polygon(new int[]{1150,1150,1250,1250,1300,1250,1250,1220,1220,1180,1180,1150,1150,1100},new int[]{450,440,440,450,425,400,410,410,350,350,410,410,400,425},14);
            Ellipse2D.Double EW=new Ellipse2D.Double(860,370,100/4,100/4);//top left light
            Ellipse2D.Double WE=new Ellipse2D.Double(1065,910,100/4,100/4);  //bottom right light
            Ellipse2D.Double N=new Ellipse2D.Double(1060,365,100/4,100/4);// top right light
            Ellipse2D.Double i1l1=new Ellipse2D.Double(315,370,100/4,100/4);//i1 top left
            Ellipse2D.Double i1l2=new Ellipse2D.Double(315,720,100/4,100/4);//i2 top left
            Ellipse2D.Double i4l1=new Ellipse2D.Double(860,720,100/4,100/4);//i4 top left
            Ellipse2D.Double i2l4=new Ellipse2D.Double(1065,560,100/4,100/4);  //bottom right light
            Ellipse2D.Double i4l4=new Ellipse2D.Double(510,560,100/4,100/4);  //i1 bottom right light
            Ellipse2D.Double i3l4=new Ellipse2D.Double(510,910,100/4,100/4);  //i1 bottom right light
            Rectangle2D back=new Rectangle2D.Float(); 
            Rectangle2D vroad1=new Rectangle2D.Float(); //Vertical road
            Rectangle2D vroad2=new Rectangle2D.Float(); //Vertical road
            Rectangle2D hroad2=new Rectangle2D.Float();
            Rectangle2D car=new Rectangle2D.Float();//Painting car
            hroad.setFrame(0,400,1920,300/2); //h1 road
            hroad2.setFrame(0,750,1920,300/2);       //Setting dimensions of shapes
            vroad1.setFrame(900,0,150,1000);
            vroad2.setFrame(350,0,150,1000);
            back.setFrame(0,330,1920,700);
            g2.draw(hroad);
            g2.draw(hroad2); //Drawing Horizontal Road
            g2.draw(vroad1);
            g2.draw(vroad2);//Drawing Vertical Road
            g2.setColor(Color.LIGHT_GRAY);
            g2.draw(back);
            g2.fill(back);
            g2.setColor(Color.black);
            g2.fill(hroad);
            g2.fill(hroad2);
            g2.fill(vroad1);
            g2.fill(vroad2);
            g2.setColor(Color.white);  
            g2.draw(new Line2D.Double(500,550,900,550));
            g2.draw(new Line2D.Double(0,550,350,550)); //h1 road bottom left line
            g2.draw(new Line2D.Double(1050,550,1920,550));//h1 road bottom right line
            g2.draw(new Line2D.Double(975,0,975,400));//v(org) road mid line
            g2.draw(new Line2D.Double(975,550,975,750));//v(org) road mid line
            g2.draw(new Line2D.Double(975,900,975,1100));//v(org) road mid line
            g2.draw(new Line2D.Double(425,0,425,400));//v(org) road mid line
            g2.draw(new Line2D.Double(425,550,425,750));//v(org) road mid line
            g2.draw(new Line2D.Double(425,900,425,1100));//v(org) road mid line
            g2.draw(new Line2D.Double(0,475,350,475));
            g2.draw(new Line2D.Double(0,475,350,475));//horizontal road 1 left side mid line
            g2.draw(new Line2D.Double(500,475,900,475));
            g2.draw(new Line2D.Double(1050,475,1920,475));//horizontal road 1 right side mid line
            g2.draw(new Line2D.Double(0,750,350,750));//h2 road top left line
            g2.draw(new Line2D.Double(500,750,900,750));
            g2.draw(new Line2D.Double(0,825,350,825));
            g2.draw(new Line2D.Double(500,825,900,825));//horizontal road 1 left side mid line
            g2.draw(new Line2D.Double(1050,825,1920,825));//horizontal road 1 right side mid line
            g2.draw(new Line2D.Double(1050,750,1920,750));//h2 top right line
            g2.draw(new Line2D.Double(0,900,350,900));//h2 road bottom left line
            g2.draw(new Line2D.Double(500,900,900,900));
            g2.draw(new Line2D.Double(1050,900,1920,900));//h2 road bottom right line
            Rectangle2D inter1=new Rectangle2D.Float();//intersection 1 centre
            Rectangle2D inter2=new Rectangle2D.Float();//intersection 2 centre
            Rectangle2D inter3=new Rectangle2D.Float();//intersection 3 centre
            Rectangle2D inter4=new Rectangle2D.Float();//intersection 4 centre
            inter1.setFrame(970,470,10,10);
            inter2.setFrame(970,820,10,10);
            inter3.setFrame(425,470,10,10);
            inter4.setFrame(425,820,10,10);
            g2.draw(inter1);
            g2.fill(inter1);
            g2.draw(inter2);
            g2.fill(inter2);
            g2.draw(inter3);
            g2.fill(inter3);
            g2.draw(inter4);
            g2.fill(inter4);
            if(traffic_light==0) //Drawing traffic signals according to the traffic light
            {  
                g2.setColor(Color.BLACK);
                g2.draw(EW);
                g2.draw(N);
                g2.draw(WE);
                g2.draw(i1l2);
                g2.draw(i1l1);
                g2.draw(i2l4);
                g2.draw(i4l1);
                g2.draw(i4l4);
                g2.draw(i3l4);
                g2.setColor(Color.green);
                g2.fill(EW);
                g2.fill(i1l1);
                g2.fill(i1l2);
                g2.fill(i4l1);
                g2.setColor(Color.red);
                g2.fill(N);
                g2.fill(WE);
                g2.fill(i2l4);
                g2.fill(i3l4);
                g2.fill(i4l4);
                
                
            }
            else if(traffic_light==1)
            { 
                g2.setColor(Color.black);
                g2.draw(WE);
                g2.draw(i1l2);
                g2.draw(EW);
                g2.draw(N);
                g2.draw(i4l1);
                g2.draw(i1l1);
                g2.draw(i2l4);
                g2.draw(i4l4);
                g2.draw(i3l4);
                g2.setColor(Color.GREEN);
                g2.fill(WE);
                g2.fill(i2l4);
                g2.fill(i4l4);
                g2.fill(i3l4);
                g2.setColor(Color.red);
                g2.fill(EW);
                g2.fill(i1l1);
                g2.fill(i1l2);
                g2.fill(i4l1);
                g2.fill(N);
                
                
                
            }
            else
            {   
                g2.setColor(Color.black);
                g2.draw(EW);
                g2.draw(i1l2);
                g2.draw(WE);
                g2.draw(N);
                g2.draw(i1l1);
                g2.draw(i4l1);
                g2.draw(i2l4);
                g2.draw(i4l4);
                g2.draw(i3l4);
                g2.setColor(Color.GREEN);
                g2.fill(N);
                g2.setColor(Color.red);
                g2.fill(EW);
                g2.fill(i1l1);
                g2.fill(i2l4);
                g2.fill(i4l1);
                g2.fill(WE);
                g2.fill(i1l2);
                g2.fill(i4l4);
                g2.fill(i3l4);
            }

            for(int i=0;i<50;i++) //For choosing color of the car and paiting the car on the map
            {
                if(carObj[i].spawn==0)
                    continue;
                
                if(carObj[i].horizontal==1)
                    car.setFrame(carObj[i].curr_x,carObj[i].curr_y,100,40);
                if(carObj[i].vertical==1)
                    car.setFrame(carObj[i].curr_x,carObj[i].curr_y,40,100);
                if(carObj[i].colour==0)
                    g2.setColor(Color.red);
                if(carObj[i].colour==1)
                    g2.setColor(Color.white);
                if(carObj[i].colour==2)
                    g2.setColor(Color.blue);
                if(carObj[i].colour==3)
                    g2.setColor(Color.green);
                if(carObj[i].colour==4)
                    g2.setColor(Color.MAGENTA);
                if(carObj[i].colour==5)
                    g2.setColor(Color.gray);
                if(carObj[i].colour==6)
                    g2.setColor(Color.cyan);
                if(carObj[i].colour==7)
                    g2.setColor(Color.yellow);
                if(carObj[i].colour==8)
                    g2.setColor(Color.LIGHT_GRAY);
                if(carObj[i].colour==9)
                    g2.setColor(Color.pink);
                
                g2.draw(car);
                g2.fill(car);       
            } 
        }   
    }
}
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton(); //Start Simulator Button
        jLabel1 = new javax.swing.JLabel(); 
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(1920, 1080));
        getContentPane().setLayout(null);

        jButton1.setFont(new java.awt.Font("Calibri", 0, 26)); // NOI18N
        jButton1.setForeground(new java.awt.Color(170, 45, 45));
        jButton1.setText("Start Simulator");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(700, 587, 330, 70);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Permanent ", 1, 90)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Traffic Simulator");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(490, 230, 1110, 120);

        jLabel3.setFont(new java.awt.Font("Helvetica", 0, 90)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        
        getContentPane().add(jLabel3);
        jLabel3.setBounds(520, 330, 940, 100);
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 1920, 1080);
        jLabel2.setIcon(iconLogo);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false); //Starting the simulation
        Driver driverObj = new Driver();
        driverObj.frame.setVisible(true);
        driverObj.start();// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewJFrame frame=new NewJFrame();
                frame.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}


