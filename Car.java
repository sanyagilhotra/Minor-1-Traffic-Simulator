class Car //This class contains the basic information of the car and used to create cars
{
    int curr_x,curr_y;//Defines current position of car
    int speed;
    int horizontal = 0, vertical = 0;//if car is in horizontal or vertical motion
    
    int turn=0;//1 if the car will turn left, 2 if car will turn right, 0 if it doesn't turn
    int dest_sel;
    String source,destination;//Source and destination of car
    int spawn=0; //To check if the car has spawned on the map or not
    int colour=0; //To provide a color to the car
    int moving=0;//Not used till now, would be used when providing stop mechanism
    int path;//Determines what lane the car has taken
    void set()  //Sets the route taken by car according to lane of car
    {
    switch (path) {
        case 0:    //h1 left tp right
            curr_x=-100;
            curr_y=420;
            source="E";
            horizontal=1;vertical=0;
            break;
        
        case 1:     //h2 left to right
            curr_x=-100;
            curr_y=765;
            source="E";
            horizontal=1;vertical=0;
            break;
        case 2:   //h1 right to left
            curr_x=1920;
            curr_y=490;
            source="W";
            horizontal=1;vertical=0;
            break;
        case 3:   //h2 right to left
            curr_x=1920;
            curr_y=840;
            source="W";
            horizontal=1;vertical=0;
            break;
        case 4:    //North to South v1
            curr_x=995;
            curr_y=-100;
            source="N";
            horizontal=0;vertical=1;
            break;
        // case 5:    //North to South v2
        //     curr_x=;
        //     curr_y=-100;
        //     source="N";
        //     horizontal=0;vertical=1;
        //     break;
        // case 6:    //South to North v1
        //     curr_x=920;
        //     curr_y=1100;
        //     source="S";
        //     horizontal=0;vertical=1;
        //     break;
        // case 7:    //South to north v2
        //     curr_x=362;
        //     curr_y=1100;
        //     source="S";
        //     horizontal=0;vertical=1;
        //     break;
        
    }
    if((source=="E" && destination=="W") || (source=="W" && destination=="E")|| (source=="N" && destination=="S")|| (source=="S" && destination=="N"))//Car won't take a turn if it going from East to West or Vice versa
        turn=0;
    if((source=="E" && destination=="N") || (source=="N" && destination=="W")|| (source=="W" && destination=="S")|| (source=="S" && destination=="E"))//Car will take a left turn
        turn=1;
    if((source=="W" && destination=="N") || (source=="N" && destination=="E")|| (source=="E" && destination=="S")|| (source=="S" && destination=="W"))//Car will take a right turn
        turn=2;
    }
}
