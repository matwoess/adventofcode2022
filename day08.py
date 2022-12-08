from pathlib import Path
import numpy as np


def pre_process_data(input_data: str) -> np.ndarray:
    array = np.array([list(d) for d in input_data.splitlines()], dtype=int)
    return array


def get_direction_vectors(tree_map: np.ndarray, r: int, c: int) \
        -> tuple[np.ndarray, np.ndarray, np.ndarray, np.ndarray]:
    up = tree_map[:r, c][::-1]
    down = tree_map[r + 1:, c]
    left = tree_map[r, :c][::-1]
    right = tree_map[r, c + 1:]
    return up, down, left, right


def part1(tree_map: np.ndarray) -> int:
    print(tree_map)
    n_rows, n_cols = tree_map.shape
    n_visible = 2 * (n_rows + n_cols - 2)  # perimeter
    for r in range(1, n_rows - 1):
        for c in range(1, n_cols - 1):
            tree_height = tree_map[r, c]
            up, down, left, right = get_direction_vectors(tree_map, r, c)
            if (
                    np.all(up < tree_height)
                    or np.all(down < tree_height)
                    or np.all(left < tree_height)
                    or np.all(right < tree_height)
            ):
                n_visible += 1
    return n_visible


def get_scene_scores(tree_map: np.ndarray) -> np.ndarray:
    n_rows, n_cols = tree_map.shape
    scene_scores = np.zeros_like(tree_map)

    def tree_count(direction_vec: np.ndarray, max_height: int) -> int:
        for i, height in enumerate(direction_vec):
            if height >= max_height:
                return i + 1
        return len(direction_vec)

    for r in range(0, n_rows):
        for c in range(0, n_cols):
            tree_height = tree_map[r, c]
            up, down, left, right = get_direction_vectors(tree_map, r, c)
            up_trees = tree_count(up, tree_height)
            down_trees = tree_count(down, tree_height)
            left_trees = tree_count(left, tree_height)
            right_trees = tree_count(right, tree_height)
            scene_scores[r, c] = up_trees * down_trees * left_trees * right_trees
    return scene_scores


def part2(tree_map: np.ndarray) -> int:
    scene_scores = get_scene_scores(tree_map)
    return np.max(scene_scores)


if __name__ == '__main__':
    raw_data = Path('inputs/day08.txt').read_text(encoding='utf-8')
    data = pre_process_data(raw_data)
    answer1 = part1(data)
    print('Answer1:', answer1)  # 1546
    answer2 = part2(data)
    print('Answer2:', answer2)  # 519064
