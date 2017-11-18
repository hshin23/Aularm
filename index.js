import React, {Component} from 'react';
import {AppRegistry} from 'react-native';
import AnalogClock from './AnalogClock';

class Analogclock extends Component {
    render() {
        return (
            <AnalogClock />
        );
    }
}


AppRegistry.registerComponent('Analogclock', () => Analogclock);
