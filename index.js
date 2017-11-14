import React, {Component} from 'react';
import {AppRegistry} from 'react-native';
import App from './app/App';

class clock extends Component {
    render() {
        return (
            <App />
        );
    }
}

AppRegistry.registerComponent('aularm', () => clock);
