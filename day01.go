package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

type Elf struct {
	foods []int
}

func preProcessData(text string) []Elf {
	elfInputs := strings.Split(text, "\n\n")
	elves := make([]Elf, len(elfInputs))
	for i, elfInput := range elfInputs {
		calories := strings.Split(elfInput, "\n")
		elves[i] = Elf{foods: make([]int, len(calories))}
		var elf = elves[i]
		for j, calStr := range calories {
			if calStr == "" {
				continue
			}
			cals, err := strconv.Atoi(calStr)
			check(err)
			elf.foods[j] = cals
		}
	}
	return elves
}

func sumIntSlice(slice []int) int {
	var sum int
	for _, val := range slice {
		sum += val
	}
	return sum
}

func maxIntSlice(slice []int) int {
	max := 0
	for i, val := range slice {
		if i == 0 || val > max {
			max = val
		}
	}
	return max
}

func maxNIntSlice(slice []int, n int) []int {
	sort.Ints(slice)
	return slice[len(slice)-n:]
}

func Map[T, R any](in []T, f func(T) R) []R {
	out := make([]R, len(in))
	for i, x := range in {
		out[i] = f(x)
	}
	return out
}

func part1(elves []Elf) int {
	calorieSums := Map(elves, func(elf Elf) int { return sumIntSlice(elf.foods) })
	maxCalories := maxIntSlice(calorieSums)
	return maxCalories
}

func part2(elves []Elf) int {
	calorieSums := Map(elves, func(elf Elf) int { return sumIntSlice(elf.foods) })
	top3CalorieSums := maxNIntSlice(calorieSums, 3)
	sumTop3 := sumIntSlice(top3CalorieSums)
	return sumTop3
}

func main() {
	dat, err := os.ReadFile("inputs/day01.txt")
	check(err)
	processedData := preProcessData(string(dat))
	answer1 := part1(processedData)
	fmt.Printf("Answer 1: %d\n", answer1) // 68802
	answer2 := part2(processedData)
	fmt.Printf("Answer 2: %d\n", answer2) // 205370
}

func check(e error) {
	if e != nil {
		panic(e)
	}
}
