# PopularMovies
My work for Udacity Android Nanodegree's Project 2 Stage 1, Popular Movies.

## How to add your API key
Grab an api key from <a href="https://www.themoviedb.org/documentation/api?language=en">The Movie DB</a>, add a xml string resource named "api_key" with the value being the actual api key to the project, and you should be ready to go. 

## A few takeaways
<ul>
  <li><b>MVVM</b> architecture</li>
  <li><b>Retrofit</b> to call apis in the viewmodel</li>
  <li><b>Picasso</b> to load images (posters and backdrops) into views</li>
  <li>Recyclerview with <b>GridLayoutManager</b> for movie list</li>
  <li>Endless Scrolling by using addtional api calls every time the end of list is reached</li>
  <li>Menu items for two different modes (Popular vs. Top Rated)</li>
  <li>ConstraintLayout and a dark theme (with inspiration from Android App <b>Atom</b>)</li>
</oul>
