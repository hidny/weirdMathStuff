## Cuboid Net Searcher

### Purpose
The purpose of the jar file is to search for nets that fold into 2 different cuboids in a way where you can choose which slice of the search space the program should work on, so it
could be easily distributed. For big cuboids, it would take an individual CPU years to complete, so it's best to divide the work up, and get it done faster.

The way to run this is by simply setting up the properties you want in net_search.properties, and running it.
You could run it by double-clicking it, or running "java -jar cuboid_net_search.jar" in the command line. I believe double-clicking it makes it faster, but I haven't really tested it.


As it runs in the background, it will output to a new file in the folder path you specified in net_search.properties, or if you didn't specify a folder path,
it will output into the 'net_search_output' folder which will be in the same directory as the READ_ME.md file.
I advise checking the output file to make sure that there were no errors.

I didn't bother making any windows or alert messages telling you it's running. The way to tell is by checking that it's running by checking if the new file is being updated,
or by checking if you can see "Java(TM) Platform SE binary" in the list of task manager processes taking up at least 10% of CPU.
If you want to stop the program, feel free to kill it from the task manager anytime you want.


### How to Configure the properties file (TODO)


2 Million pieces, batch, separate machine,
how to start
how to set path windows
default path



example configs:
```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=4000
batch_index_to_search=21
output_folder=D:\\net_search_output\\
```
(Because of java, we need two backslashes for paths with backslashes)

output path:
net_search_13x1x1_and_3x3x3_SD_13_BS_4000_IND_21.txt
general format:
net_search_{cuboid1}_and_{cuboid2}_SD_{search_start_depth}_BS_{batch_size}_IND_{batch_index_to_search}.txt

First index searched = {batch_size} * {batch_index_to_search}
last index searched = ({batch_size} + 1) * {batch_index_to_search} -1

The index shuffle:

I shuffled the slices based on the RSA encryption. I did it because I wanted to make every batch a randomish sample of
the search space, so I can make better predictions about how long it will take and how many solutions there are.
I'm aware that this complicates things and makes it more confusing, but I like to be able to predict.

Equation for getting the index after the shuffle given index i before the shuffle:

f(i) = i^17 mod N

Where N is the produce of two primes equal to or closest over the number of slices available.

See wikipedia for RSA for more details.



### Example configurations (TODO)

other example:
```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=4000
batch_index_to_search=21
```
Default path should be inside of {current_dir}/net_search_output/


other example:
```
cuboid1=5x3x1
cuboid2=1x1x11
search_start_depth=13
batch_size=4000
batch_index_to_search=21
```
Default path should be inside of {current_dir}/net_search_output/
The cuboids will be rearranged to the standard form where the Nx1x1 cuboid is at the front and
will be written like this "11x1x1"
File name for this example:
net_search_11x1x1_and_5x3x1_SD_13_BS_4000_IND_21.txt


### Configurations for small cuboids to sanity test it