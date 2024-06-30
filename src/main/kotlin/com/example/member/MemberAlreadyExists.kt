package com.example.member

class MemberAlreadyExists(): Exception(
    "There is already a user with the same username in the room")