####Authors: Sadie Lombardi <slombardi@wpi.edu>, Alan Healy <?>, Billy Cross <?>
Date: October 4th 2021, 10/4/21

###Description:

This program serves as a player for Othello, a popular board game that involves capturing enemy pieces via diagonals, horizontals, and verticals.
Saladin uses minimax and alpha beta pruning to determine the next move it makes based on the current board. 
It interacts with the Referee.py program by reading the move_file and Saladin.go file. The Saladin.go file indicates
that it is our player's turn, and it starts searching for moves as soon as it sees that file in the directory.
Our evaluation of the moves is based off of the current number of our/enemy moves on the board, and how many valid moves are available
at the next board state, given any valid move.

###Prerequisites:
* Python 3.5+
* Java 15+

Usage:
Run the referee program:
python Referee.py <GROUP 1> <GROUP 2>

Run Saladin.jar:
java -jar Saladin.jar <GROUP 1>