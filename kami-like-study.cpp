#include <iostream>
#include <cstring>
using namespace std;

#define LINES 16
#define COLS 10

class Game
{
public:
	Game() { }

	virtual ~Game() { }

	void init()
	{
		for(int i = 0; i < LINES; i++)
			for(int j = 0; j < COLS; j++)
				_current[i][j] = 0;
	}

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
	}

private:
	int _current[LINES][COLS];
};

int main(int argc, char* argv[])
{
	Game game;
	char init[] =
	"1111111111"
	"1111111111"
	"1111111111"
	"1111111111"
	"1111111111"
	"1111111111"
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

	if (game.init(init))
	{
		game.print();
	}

	return 0;
}
