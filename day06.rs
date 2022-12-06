use std::{fs, collections::HashSet};

fn first_unique_window_end(buffer: &String, window_size: usize) -> usize {
    for idx in (window_size - 1)..buffer.len() {
        let mut set: HashSet<char> = HashSet::new();
        let window = buffer.get((idx - (window_size - 1))..=idx).unwrap();
        set.extend(window.chars());
        if set.len() == window_size {
            return idx + 1;
        }
    }
    return usize::MAX;
}

fn part1(buffer: &String) -> usize {
    first_unique_window_end(buffer, 4)
}

fn part2(buffer: &String) -> usize {
    first_unique_window_end(buffer, 14)
}

fn main() {
    let data: String = fs::read_to_string("inputs/day06.txt").unwrap();
    let answer1 = part1(&data);
    println!("Answer 1: {}", answer1);  // 1760
    let answer2 = part2(&data);
    println!("Answer 2: {}", answer2);  // 2974
}