#include <iostream>
#include <cstring>
#include <chrono>
#include <thread>
using namespace std;

#define LINES 16
#define COLS 10

class Game
{
public:
	Game() { }

	virtual ~Game() { }

	void init(int game[LINES][COLS])
	{
		for(int i = 0; i < LINES; i++)
			for(int j = 0; j < COLS; j++)
				_current[i][j] = game[i][j];
	}

	bool init(const char game[])
	{
		int game_int[LINES][COLS];
		for(int i = 0; i < LINES; i++)
			for(int j = 0; j < COLS; j++)
				game_int[i][j] = (int)(game[i*COLS+j] - '0');
		init(game_int);
	}

	void print()
	{
		for(int i = 0; i < LINES; i++)
		{
			for(int j = 0; j < COLS; j++)
				cout<<_current[i][j];
			cout<<endl;
		}
		this_thread::sleep_for(chrono::milliseconds(10));
	}

	void fill(int line, int col, int color)
	{
		if (line>=0 && line<LINES && col>=0 && col<COLS && (color!=_current[line][col]))
			fill(line, col, color, _current[line][col]);
	}

private:
	void fill(int line, int col, int color, int color_bg)
	{
		if (line>=0 && line<LINES && col>=0 && col<COLS && _current[line][col]==color_bg)
		{
			_current[line][col]=color;
			fill(line-1, col, color, color_bg);
			fill(line+1, col, color, color_bg);
			fill(line, col-1, color, color_bg);
			fill(line, col+1, color, color_bg);
		}
	}

	void cursor_back(int lines, int cols)
	{
		printf("\033[%dA\033[%dD", lines, cols);
	}

	int _current[LINES][COLS];
};

int main(int argc, char* argv[])
{
	Game game;
	char init[] =
	"1111111111"
	"1111111111"
	"1111111111"
	"1114111111"
	"1114111111"
	"1114444411"
	"1111111111"
	"1111111111"
	"0000000000"
	"0000000000"
	"0000000000"
	"0000000000"
	"0000000000"
	"0000000000"
	"0000000000"
	"0000000000";

	game.init(init);
	game.fill(3,3,2);
	game.fill(10,0,3);
	game.fill(3,3,3);

	return 0;
}
