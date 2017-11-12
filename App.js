/* Library Imports  */
import React, {Component} from 'react';

import {
    Platform,
    StyleSheet,
    Text,
    Image,
    View
} from 'react-native';
import firebase from 'react-native-firebase';

/* Style Declaration for View */
const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#000',
        justifyContent: 'center',
        alignItems: 'center'
    },
    timeText: {
        color: '#999999',
        fontSize: 80
    }
});

/* Class Declaration for View */
export default class App extends Component < {} > {
    constructor(props) {
        super(props);

        this.state = {
            time: "9123",
            date: "1234"
        };
    }

    render() {
        let pic = {
        };
        return (
            <View style={styles.container}>
                <Text style={styles.timeText}>
                    {this.state.time}
                </Text>
                <Text>
                    {this.state.date}
                </Text>
                <Image source={{uri: 'https://upload.wikimedia.org/wikipedia/commons/d/de/Bananavarieties.jpg'}} style={{width: 193, height: 110}}/>
            </View>
        );
    }
}
