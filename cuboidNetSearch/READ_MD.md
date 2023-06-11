## Cuboid Net Searcher

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