## Cuboid Net Searcher

### Purpose
The purpose of the jar file is to search for nets that fold into 2 different cuboids in a way where you can choose which slice of the search space the program should work on, so it could be easily distributed. For big cuboids, it would take an individual CPU years to complete, so it's best to divide the work up, and get it done faster.

The way to run this is by simply setting up the properties you want in net_search.properties inside the cuboidNetSearch folder (same folder as README.md), and running it.
You could run it by double-clicking it, or running "java -jar cuboid_net_search.jar" in the command line. I believe double-clicking it makes it faster, but I haven't really tested it.

As it runs in the background, it will output to a new file in the folder path you specified in net_search.properties, or if you didn't specify a folder path, it will output into the 'net_search_output' folder which will be in the same directory as the READ_ME.md file. I advise checking the output file to make sure that there were no errors.

I didn't bother making any windows or alert messages telling you it's running. The way to tell is by checking that it's running by checking if the new file is being updated, or by checking if you can see "Java(TM) Platform SE binary" in the list of task manager processes taking up at least 10% of CPU.
If you want to stop the program, feel free to kill it from the task manager anytime you want.

In my experience, I get the best results when I run 3 of these at once, but if you actually want to be able to use your computer as it runs,
maybe only have 1 or 2 running in the background at once.

### How to Configure the properties file

The properties file has 6 options that affect what it will do. This section will explain each one. Just for reference, here's an example configuration the properties file:
```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=4000
batch_index_to_search=21
output_folder=D:\\net_search_output\\
```
#### cuboid 1 and 2
List the dimensions of the 2 cuboids you want to search. The program will error out if the 2 cuboids don't have the same area. I didn't make it check if the 2 cuboids are the same, but you shouldn't make them the same because you will find too many solutions, and it will cause some memory problems.
Another thing to note is that the 1st cuboid should be of the form Nx1x1. If it's not, the program will make adjustments, so it will happen, but to reduce confusion please just make it of the form 'Nx1x1'.

#### search_start_depth

This number tells the program how many cells/squares/tiles to start from. 
The ways the program divides the problem into slices is by having an index for every possible configuration of a net with 'search_start_depth' cells/squares/tiles inside. The bigger the depth, the more indexes you have to search, but the faster each index is to search. For example, because we set this to 13, and there is around 2 million configurations/states of a net with 13 square, there will be 2 million indexes to search. If the depth was 12, the number of indexes to search would be less, but each index would take longer.

The acceptable range is probably from 1 to 15.

#### batch_size
How many indexes you want to search in a single run. The idea is to do more than 1 because then the time it takes won't be as skewed. (It's just the law of large numbers)

#### batch_index_to_search
Determines which batch of indexes to search.
The first index searched = {batch_size} * {batch_index_to_search}
The last index searched = ({batch_size} + 1) * {batch_index_to_search} -1
The number of indexes that will be searched is equal to {batch_size}.

#### output_folder
The output folder of the program. Unfortunately, because of java, we need two backslashes for paths with backslashes. Note that you can't specify the filename, because that's created in a standardized way. This is the only field that optional. if you didn't specify a folder path, it will output into the 'net_search_output' folder which will be in the same directory as the READ_ME.md file.

### Other Details About the Implementation

#### standardized output path

general format:
```
net_search_{cuboid1}_and_{cuboid2}_SD_{search_start_depth}_BS_{batch_size}_IND_{batch_index_to_search}.txt
```
Example:
```
net_search_13x1x1_and_3x3x3_SD_13_BS_4000_IND_21.txt
```

### The Index Shuffle Algo

I made the code slightly more complicated by shuffling the indexes. I did it because I wanted to make every batch a pseudo-random sample of the search space, so I can make better predictions about how long it will take and how many solutions there are. I based it on the RSA encryption scheme: https://en.wikipedia.org/wiki/RSA_(cryptosystem)

I'm aware that this complicates things and makes it more confusing, but I like to be able to predict. After I did it, I googled 'RSA shuffle', and apparently it's not a thing, but it works!

Here's the equation for getting the index after the shuffle given index i before the shuffle:

F(index) = index^17 mod N

Where N is the product of two primes equal to or closest over the number of slices available.

See wikipedia for RSA for more details.



### Example configurations 
```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=4000
batch_index_to_search=21
output_folder=D:\\net_search_output\\
```
This searches for a net that folds the 13x1x1 cuboid and the 3x3x3 cuboid starting at depth 13 (13 tiles in the net already places) at index 84000 (21 * 4000), and ending at index 8799. It will output the file to the folder: 'D:\\net_search_output\\'

```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=4000
batch_index_to_search=21
```
Same as previous, but it will save to the default path which should be inside of {current_dir}/net_search_output/

```
cuboid1=5x3x1
cuboid2=1x1x11
search_start_depth=13
batch_size=4000
batch_index_to_search=21
```
Default path should be inside of {current_dir}/net_search_output/
The cuboids will be rearranged to the standard form where the Nx1x1 cuboid is at the front and
will be written like this "11x1x1".
File name for this example:
net_search_11x1x1_and_5x3x1_SD_13_BS_4000_IND_21.txt


### Configurations for small cuboids to get quick results and to sanity test it

```
cuboid1=5x1x1
cuboid2=3x2x1
search_start_depth=5
batch_size=4000
batch_index_to_search=0
```
This should get you 2263 solutions. This should take less than 2 minutes.

```
cuboid1=2x1x1
cuboid2=2x1x1
search_start_depth=5
batch_size=4000
batch_index_to_search=0
```
This should get you 723 solutions. This should take less than 1 minute. This just gets the solutions for the 2x1x1 cuboid.
Please don't do this for bigger Nx1x1 cuboids because there's lots of solutions and it will be a waste of disk space.


```
cuboid1=7x1x1
cuboid2=3x3x1
search_start_depth=5
batch_size=1000
batch_index_to_search=0
```
This should get you 1070 solutions. This should take less than 1 hour. If you want the 1080 solutions that previous papers say exist, you will have to use another version of the jar
that accepts inviable cuts in the net. I might provide that on a later date.

```
cuboid1=13x1x1
cuboid2=3x3x3
search_start_depth=13
batch_size=1000
batch_index_to_search=41
```
This searches for cuboids that fold into both the 13x1x1 cuboid and the 3x3x3 cuboid. It starts at index 41000 (41 * 1000) and ends at index 41999 (41000 + 1000 - 1).
I believe this will find 18 solutions.
As of this writing, I'm looking for all the 13x1x1 and 3x3x3 solutions, and I'm a bit discouraged because it seems like there will be less than 5000 solutions, which is much less than the over 4 million solutions that fold the 11x1x1 and 5x3x2 cuboids.
On average, searching 1000 indexes takes around 6 hours, so searching the full 2 million indexes with 3 instances running will take around 170 days, or 6 months.

Time = 2 million index * (6 hours / 1000 (indexes per core)) / 3 cores / (24 hours /day) = 167 days