from pathlib import Path
import numpy as np
import astar  # pip install --user astar


def pre_process_data(input_data: str) -> (np.ndarray, tuple[int, int], tuple[int, int]):
    char_array = np.array([list(row) for row in input_data.splitlines()], dtype=str)
    start_pos = tuple(np.argwhere(char_array == 'S')[0])
    end_pos = tuple(np.argwhere(char_array == 'E')[0])
    int_view = char_array.view(np.int32) - ord('a')
    int_view[start_pos] = 0
    int_view[end_pos] = ord('z') - ord('a')
    return int_view, start_pos, end_pos


def print_path(path: list, bounds: tuple[int, int]):
    n_rows, n_cols = bounds
    for r in range(n_rows):
        for c in range(n_cols):
            print_char = '.'
            if (r, c) in path:
                curr_idx = path.index((r, c))
                if curr_idx == 0:
                    print_char = 'S'
                elif curr_idx == len(path) - 1:
                    print_char = 'E'
                else:
                    next_node = path[curr_idx + 1]
                    if c == next_node[1]:
                        print_char = "v" if r < next_node[0] else "^"
                    elif r == next_node[0]:
                        print_char = ">" if c < next_node[1] else "<"
            print(print_char, end='')
        print()


class PathFinder(astar.AStar):
    def __init__(self, landscape: np.ndarray):
        self.landscape = landscape
        self.n_rows, self.n_cols = landscape.shape

    def heuristic_cost_estimate(self, current, goal) -> float:
        # manhattan distance
        return abs(goal[0] - current[0]) + abs(goal[1] - current[1])

    def distance_between(self, n1, n2):
        return 1

    def neighbors(self, node):
        for r, c in self.adjacent_nodes(node):
            if self.landscape[r, c] <= self.landscape[node[0], node[1]] + 1:
                yield r, c

    def adjacent_nodes(self, node):
        up_down_left_right = (+1, 0), (-1, 0), (0, -1), (0, +1)
        for r, c in ((node[0] + direction[0], node[1] + direction[1]) for direction in up_down_left_right):
            if 0 <= r < self.n_rows and 0 <= c < self.n_cols:
                yield r, c


def part1(landscape: np.ndarray, start_pos: int, end_pos: int) -> int:
    solver = PathFinder(landscape)
    result = solver.astar(start_pos, end_pos)
    path = list(result)
    print_path(path, bounds=landscape.shape)
    return len(path) - 1


def part2(landscape: np.ndarray, _, end_pos: int) -> int:
    solver = PathFinder(landscape)
    starting_points = list(map(tuple, np.argwhere(landscape == 0)))
    paths = [list(res) for res in (solver.astar(sp, end_pos) for sp in starting_points) if res is not None]
    valid_paths_lengths = [len(path) - 1 for path in paths]
    return min(valid_paths_lengths)


if __name__ == '__main__':
    raw_input = Path('inputs/day12.txt').read_text(encoding='utf-8')
    data = pre_process_data(raw_input)
    answer1 = part1(*data)
    print('Answer1:', answer1)  # 517
    answer2 = part2(*data)
    print('Answer2:', answer2)  # 512
