package com.example.riseandroid.ui.screens.watchlist

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun WatchlistScreen(
//    viewModel: WatchlistViewModel = viewModel(),
//    allMovies: List<Movie>,
//    onMovieClick: (Long) -> Unit,
//    navController: NavController
//) {
//    val watchlistIds = viewModel.watchlist.collectAsState().value
//    val watchlistMovies = allMovies.filter { movie -> movie.movieId in watchlistIds }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Box(
//                        modifier = Modifier.fillMaxWidth(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "My Watchlist",
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center
//                        )
//                        IconButton(
//                            onClick = { navController.navigate("account") },
//                            modifier = Modifier
//                                .align(Alignment.CenterStart)
//                                .padding(end = 16.dp)
//                        ) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Back",
//                                tint = Color.White
//                            )
//                        }
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFFE5CB77),
//                    titleContentColor = Color.White
//                )
//            )
//        }
//    )
//    { padding ->
//        if (watchlistMovies.isEmpty()) {
//            EmptyWatchlistMessage(Modifier.padding(padding))
//        } else {
//            LazyColumn(
//                modifier = Modifier
//                    .padding(padding)
//                    .fillMaxSize()
//            ) {
//                items(watchlistMovies) { movie ->
//                    WatchlistMovieItem(movie = movie, onClick = { onMovieClick(movie.movieId) })
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmptyWatchlistMessage(modifier: Modifier = Modifier) {
//    Box(
//        modifier = modifier
//            .fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Your watchlist is empty.",
//            fontSize = 18.sp,
//            color = Color.Gray
//        )
//    }
//}
//
//@Composable
//fun WatchlistMovieItem(movie: Movie, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onClick() },
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Image(
//                painter = painterResource(id = movie.posterResourceId),
//                contentDescription = "Movie Poster",
//                modifier = Modifier
//                    .size(60.dp)
//                    .clip(RoundedCornerShape(8.dp))
//            )
//
//            Column {
//                Text(
//                    text = movie.title,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//                Text(
//                    text = movie.genre ?: "No genre",
//                    fontSize = 14.sp,
//                    color = Color.Gray
//                )
//                Text(
//                    text = movie.length ?: "No length",
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
//        }
//    }
//}
