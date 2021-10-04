####Authors: Sadie Lombardi <slombardi@wpi.edu>, Alan Healy <ajhealy@wpi.edu>, Billy Cross <wmcross@wpi.edu>
Date: October 4th 2021, 10/4/21

###Description:

####Teammember contributions

* Sadie Lombardi - I contributed to doing this program's finding valid moves functionality, Direction class for adjacent cell checking, Cell class, overall refactoring, Board class, Minimove class, finding valid moves, and internal / external testing.
* Alan Healy - I contributed to doing this program's minimax functionality, alpha beta pruning, Board class, overall refactoring, Direction class for adjacent cell checking, Board display, internal testing, capturing functionality, reading in and processing enemy moves, and MinimaxAgent class.
* Billy Cross - I contributed to doing this program's Agent class, minimax functionality, alpha beta pruning, Board class, CellColor Enum, Main functionality, internal testing, and Minimove class.

###Prerequisites:
* Python 3.5+
* Java 15+

###Usage:
Run the referee program:
python Referee.py <GROUP NAME1> <GROUP NAME2>

Run Saladin.jar:
java -jar Saladin.jar <GROUP NAME>

This program serves as a player for Othello, a popular board game that involves capturing enemy pieces via diagonals, horizontals, and verticals.

* Utility Function: Our utility function involves us counting the number of possible moves minus the number of possible enemy moves, and dividing that sum by one hundred.
* Evaluation Function: Our evaluation of the moves is based off of the current number of our/enemy pieces on the board, and how many valid moves are available at the next board state, given any valid move.
* Strategies: Our program uses minimax and alpha beta pruning to determine the next move it makes based on the current board. We limit our depth inside our heuristic function in order to avoid going over time.

####Results
* We tested our program vigorously against itself, and against another team's AI. Our AI was very fast against itself, which we assume is
because the programs are identical, and move in ways that the other can predict. When we played against another team's AI however, we timed out a couple times and then had to reevaluate our cut off for the time limit.
  * Program's strengths: Our program is very fast paced, because of our simplistic heuristic function. This means that it doesn't time out even when considering multiple board states.
  * Program's weaknesses: Our program doesn't always choose the move that will give it the maximum number of possible pieces, because we don't consider multiple moves into the future during our minimax.
    
The heuristics we chose are good because the number of moves available tells you how much mobility your AI has, which is better than just considering the captured pieces.
This is because it is more important to have more "open" future moves than to have more pieces at any given state.
More available moves means that we can have a larger potential for a higher valued capture down the line. For example, keeping the corners open to capture is a very good strategy as enemies cannot recapture anything if the corners are isolated.
The sum of those moves in our evaluation function also indicates when we have reached a tiebreaker state.
    
