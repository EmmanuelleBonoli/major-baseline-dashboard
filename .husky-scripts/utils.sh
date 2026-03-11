echo_green() {
    printf "\033[0;32m%s\033[0m\n" "$1"
}

echo_yellow() {
    printf "\033[0;33m%s\033[0m\n" "$1"
}

echo_red() {
    printf "\033[0;31m%s\033[0m\n" "$1"
}

echo_separator() {
    printf "\033[0;32m----------------------------------------------\033[0m\n"
}

echo_separator_general() {
    printf "\033[0;37m----------------------------------------------\033[0m\n"
}
