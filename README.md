# Graph Sampling
This is a project is to sample graphs efficiently.

## Usage
1. Run classes from input package.
2. Output will contain original avarage clustering coefficient and of samples created using different sampling algorithms.

## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Description
This project uses [Rank Degree Algorithm](http://ieeexplore.ieee.org/document/7752223/) as base. Project has used two graphs for testing:

1. [Yeast-Protein graph](http://vlado.fmf.uni-lj.si/pub/networks/Data/bio/Yeast/Yeast.htm)
2. [egoFacebook graph](https://snap.stanford.edu/data/egonets-Facebook.html)

Scope of this project is to analyze samples using avarage clustering coefficient as comparison criteria.
Clustering Coefficient of any vertex is number of links between neighbours of the vertex over number of all possible links between its neighbours.

Project has introduced three modifications to the Rank Degree algorithm using Greedy Approximation and Parallel Processing.

1. Greedy Rank Degree
2. Parallel Rank Degree
3. Greedy Parallel Rank Degree

## Major References
1. E. Voudigari, N. Salamanos, T. Papageorgiou and E. J. Yannakoudakis, "Rank degree: An efficient algorithm for graph sampling," 2016 IEEE/ACM International Conference on Advances in Social Networks Analysis and Mining (ASONAM), San Francisco, CA, 2016, pp. 120-129.
doi: 10.1109/ASONAM.2016.7752223
keywords: {Algorithm design and analysis;Facebook;Fires;Sampling methods;Twitter},
URL: http://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=7752223&isnumber=7752180
