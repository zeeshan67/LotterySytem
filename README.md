# LotterySytem

The system should have a way to create Lotteries. Users can participate in any lottery that isn't finished yet. When you participate to a Lottery you become part of its extraction pool of potential winners. Once the Lottery closes a random winner is extracted from the pool and save winner number in a storage.

Functional requirement:

The service will allow anyone to register as a lottery participant.
Lottery participants will be able to submit as many lottery ballots for any lottery that isn’t yet finished.
Each day at midnight the lottery event will be considered closed and a random lottery winner will be selected from all participants for the day.
All users will be able to check the winning ballot for any specific date.
The service will have to persist the data regarding the lottery.

Constraints:

User need to pass the valid token to purchase lottery tickets.
The lottery can always be started with the lottery name. The only rule is that multiple active lotteries cannot be created with the same lottery name.
The lottery ticket number has a unique sequential generated number per lottery starting at 1000 and using base62 encoding number is converted into 7 digit unique characters (eg.00000G9)


To accomplish this given task I have created two separate service one for managing the user registration and the one for the actual execution of the lottery.

For managing User registration I have purposely used RDBMS,as the data for the user is structured and in the project I am using H2 database for simplicity as it does not require seperate installation, but in real case I would prefer using postgress

Below is the Schema I have used for storing the User details.

Users

id
userId
firstName
lastName
password
role_id

role

id
name

Authentication and authorization is handled using JWT token. 

Below are the endpoint exposed by User registration service :

/User/login -> This is a post request which accepts userId and password from user , if the user is registered then it provides the token as a response
/User/register -> This is a post request which accepts user basic details such as userId,password(encrypted),firstName,lastName,mobileNumber, if the user is unique the endpoint would send token else it will send reject message. 

Note: as part of schema creation admin user is created which has admin role and it is the only user responsible for creating Lottery.

For managing the execution I have used Cassandra because the data that we want to store is based on the time range so it makes sense to store the data logically i column oriented database so that it can be queried faster and it is scabale as based on the partition key we can store the similar data on disfferent nodes along with replication.

Below is the model for storing lottery related data.

1. create table if not exists lottery (id text, name text, startDate TIMESTAMP ,endDate TIMESTAMP, winningLottery text,shardKey text, PRIMARY KEY ((shardKey),startDate,name));  -> Lottery table for storing the lottery detail, here I have created additional column as shardKey that drives the partition strategy. it is stored as yyyyMM so all the data for a specific month goes to a single node as the Lottery data will be significant lesser, hence it makes sense to store monthly data together.

2. create table if not exists lottery_ticket (id text,  lotteryId text , userId text , date TIMESTAMP , PRIMARY KEY ((lotteryId,date),id)); -> lottery_tiket table stores the ticket information , here the partition is based on the lotteryid(id of lottery table) and date


How to Run Project :
Prerequisites :

Java 8 
Docker
sbt 1.4.5


Steps for running:
1. unzip the folder and navigate to LotterySystem source folder path
2. run Cassandra by navigating to LotterySystem/LotteryExecutionService/docker (docker-compose up -d)
3. navigate back to source path and run sbt clean compile
4. once the build is complete navigate to LotterySystem/UserRegistrationService folder and run sbt run , it will ask to select main class, press 1 which is the main file for the service
5. Repeat the same process for LotteryExecutionService


In real time scenario we need to have a gateway which does the authentication backed by load balancer for routing the traffic appropriately to the required service. 
we can run multiple instances of User registration service depending on volume of user request.
We can use Redis for caching the data and storing the counter state as redis counter are thread safe we dnt have to bother about locking, currently for simplicity I am using ConcurrentHashMap for caching the data. I could have implemented LRU cache for storing the winning related data as storing in map is not efficient as we are giving privilege to user to fetch historical winner data which could eventually exhaust the resource.
Running multiple instances of LotteryExecutionService requires unique initial counter value so that the value does not overlap, it can be achieved by using zookeerper or by using tokenservice which stores the unassigned range of counter and exposes rest api to the other service to query.

I have not written test cases as the complete project was done in 3 days, but will update the repo and do the commit once I have free time.
I have added all the screenshots of the possible scenario in seperate word file for simplicity of understanding the system flow.





