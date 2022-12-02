from pathlib import Path

shape_scores = {'rock': 1, 'paper': 2, 'scissors': 3}
result_scores = {'loss': 0, 'draw': 3, 'win': 6}
letter_shape_mapping = {
    'A': 'rock',
    'B': 'paper',
    'C': 'scissors',
    'X': 'rock',
    'Y': 'paper',
    'Z': 'scissors',
}
letter_result_mapping = {'X': 'loss', 'Y': 'draw', 'Z': 'win'}
required_shape = {
    ('rock', 'loss'): 'scissors',
    ('rock', 'draw'): 'rock',
    ('rock', 'win'): 'paper',
    ('scissors', 'loss'): 'paper',
    ('scissors', 'draw'): 'scissors',
    ('scissors', 'win'): 'rock',
    ('paper', 'loss'): 'rock',
    ('paper', 'draw'): 'paper',
    ('paper', 'win'): 'scissors',
}


def get_result_score(enemy_shape: str, my_shape: str) -> int:
    match (enemy_shape, my_shape):
        case m1, m2 if m1 == m2:
            return result_scores['draw']
        case ('rock', 'paper') | ('paper', 'scissors') | ('scissors', 'rock'):
            return result_scores['win']
        case ('rock', 'scissors') | ('paper', 'rock') | ('scissors', 'paper'):
            return result_scores['loss']
        case _:
            raise RuntimeError('Invalid case!')


def part1(rounds: list[(str, str)]) -> int:
    total_score = 0
    for enemy_shape_char, my_shape_char in rounds:
        enemy_shape = letter_shape_mapping[enemy_shape_char]
        my_shape = letter_shape_mapping[my_shape_char]
        total_score += shape_scores[my_shape] + get_result_score(enemy_shape, my_shape)
    return total_score


def part2(rounds: list[(str, str)]) -> int:
    total_score = 0
    for shape_char, result_char in rounds:
        enemy_shape = letter_shape_mapping[shape_char]
        result = letter_result_mapping[result_char]
        needed_shape = required_shape[(enemy_shape, result)]
        total_score += shape_scores[needed_shape] + result_scores[result]
    return total_score


if __name__ == '__main__':
    data = Path('inputs/day02.txt').read_text(encoding='utf-8')
    split_data = [line.split(" ") for line in data.splitlines()]
    answer1 = part1(split_data)
    print('Answer1:', answer1)  # 15572
    answer2 = part2(split_data)
    print('Answer2:', answer2)  # 16098
